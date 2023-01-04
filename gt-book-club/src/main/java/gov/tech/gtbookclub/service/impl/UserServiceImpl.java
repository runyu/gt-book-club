package gov.tech.gtbookclub.service.impl;

import gov.tech.gtbookclub.Constant.AppConstant;
import gov.tech.gtbookclub.exception.ItemExistsException;
import gov.tech.gtbookclub.exception.ResourceNotFoundException;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.CreateUserRequest;
import gov.tech.gtbookclub.model.request.UpdateUserRequest;
import gov.tech.gtbookclub.repository.UserRepository;
import gov.tech.gtbookclub.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        if (userRepository.findByEmail(createUserRequest.getEmail()).isPresent()) {
            throw new ItemExistsException("User is already registered with email: " + createUserRequest.getEmail());
        }

        User user = new User();
        BeanUtils.copyProperties(createUserRequest, user);
        user.setRole(validateRoleName(createUserRequest.getRole()));
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
    public User updateUser(String email, UpdateUserRequest updateUserRequest) {
        User originUser = findUser(email);
        originUser.setName(updateUserRequest.getName() != null ? updateUserRequest.getName() : originUser.getName());
        originUser.setPassword(updateUserRequest.getPassword() != null ? bcryptEncoder.encode(updateUserRequest.getPassword()) : originUser.getPassword());
        originUser.setRole(updateUserRequest.getRole() != null ? validateRoleName(updateUserRequest.getRole()) : originUser.getRole());
        originUser.setUpdatedAt(new Date());
        return userRepository.save(originUser);
    }

    @Override
    public void deleteUser(String email) {
        User user = findUser(email);
        userRepository.delete(user);
    }

    @Override
    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for the email " + email));
    }

    @Override
    public User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for the email " + email));
    }

    private String validateRoleName(String role) throws ResourceNotFoundException {
        if (role.equalsIgnoreCase(AppConstant.ROLE_ADMIN)) {
            return AppConstant.ROLE_ADMIN;
        } else if (role.equalsIgnoreCase(AppConstant.ROLE_EDITOR)) {
            return AppConstant.ROLE_EDITOR;
        } else if (role.equalsIgnoreCase(AppConstant.ROLE_USER)) {
            return AppConstant.ROLE_USER;
        }
        throw new ResourceNotFoundException("role value must be either ROLE_ADMIN, ROLE_EDITOR or ROLE_USER");
    }
}
