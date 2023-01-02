package gov.tech.gtbookclub.security;

import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User exisitingUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("user not found for email = " + email));

        return new org.springframework.security.core.userdetails.User(exisitingUser.getEmail(), exisitingUser.getPassword(), new ArrayList<>());
    }
}
