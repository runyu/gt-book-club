package gov.tech.gtbookclub.security;

import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException("user not found for email = " + email);
        }

        User exisitingUser = userOptional.get();
        return new org.springframework.security.core.userdetails.User(exisitingUser.getEmail(), exisitingUser.getPassword(), getAuthorities(exisitingUser.getRole()));
    }

    public List<SimpleGrantedAuthority> getAuthorities(String role){
        List<SimpleGrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(role));
        return list;
    }
}
