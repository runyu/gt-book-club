package gov.tech.gtbookclub.service.impl;

import gov.tech.gtbookclub.exception.ItemExistsException;
import gov.tech.gtbookclub.exception.ResourceNotFoundException;
import gov.tech.gtbookclub.model.dto.UserModel;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.repository.UserRepository;
import gov.tech.gtbookclub.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public User createUser(UserModel userModel) {
        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            throw new ItemExistsException("User is already registered with email: " + userModel.getEmail());
        }

        User user = new User();
        BeanUtils.copyProperties(userModel, user);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        user.setUpdatedAt(new Date());
        user.setCreatedAt(new Date());
        return userRepository.save(user);
    }

    @Override
    public User readUser() throws ResourceNotFoundException {
        String email = getLoggedUser().getEmail();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found for the email: " + email));
    }

    @Override
    public User updateUser(UserModel user) {
        User originUser = readUser();
        originUser.setName(user.getName() != null ? user.getName() : originUser.getName());
        originUser.setEmail(user.getEmail() != null ? user.getEmail() : originUser.getEmail());
        originUser.setPassword(user.getPassword() != null ? bcryptEncoder.encode(user.getPassword()) : originUser.getPassword());
        originUser.setUpdatedAt(new Date());
        return userRepository.save(originUser);
    }

    @Override
    public void deleteUser() {
        User user = readUser();
        userRepository.delete(user);
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for the email " + email));
    }

}
