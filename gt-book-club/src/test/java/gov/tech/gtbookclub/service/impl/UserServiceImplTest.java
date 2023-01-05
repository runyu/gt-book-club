package gov.tech.gtbookclub.service.impl;

import gov.tech.gtbookclub.exception.ItemExistsException;
import gov.tech.gtbookclub.exception.ResourceNotFoundException;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.CreateUserRequest;
import gov.tech.gtbookclub.model.request.UpdateUserRequest;
import gov.tech.gtbookclub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder bcryptEncoder;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void createUser_ItemExistsException() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("email@email");

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        assertThrows(ItemExistsException.class, () -> userService.createUser(createUserRequest));
    }

    public User mock_user() {
        return User.builder()
                .id("id1")
                .name("name1")
                .email("email@email1")
                .password("password1")
                .role("ROLE_USER")
                .build();
    }

    @Test
    public void createUser_SUCCESS() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("name");
        createUserRequest.setEmail("email@email");
        createUserRequest.setPassword("password");
        createUserRequest.setRole("ROLE_USER");

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(mock_user());

        User user = userService.createUser(createUserRequest);
        assertEquals(user.getName(), "name1");
        assertEquals(user.getEmail(), "email@email1");
        assertEquals(user.getPassword(), "password1");
        assertEquals(user.getRole(), "ROLE_USER");
    }

    @Test
    public void readUser_SUCCESS() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(mock_user()));
        Authentication authentication = Mockito.mock(Authentication.class);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertEquals(userService.readUser().getEmail(), "email@email1");
    }


    @Test
    public void readUser_UsernameNotFoundException() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.readUser());
    }

    public void mock_findUser_SUCCESS() {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(mock_user()));
    }

    @Test
    public void updateUser_SUCCESS_ROLE_ADMIN() {
        mock_findUser_SUCCESS();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("name2");
        updateUserRequest.setPassword("password2");
        updateUserRequest.setRole("ROLE_ADMIN");

        User user = new User();
        BeanUtils.copyProperties(updateUserRequest,user );

        Mockito.when(userRepository.save(any())).thenReturn(user);
        User updatedUser = userService.updateUser("email@email", updateUserRequest);

        assertEquals(updatedUser.getName(), "name2");
        assertEquals(updatedUser.getPassword(), "password2");
        assertEquals(updatedUser.getRole(), "ROLE_ADMIN");
    }

    @Test
    public void updateUser_SUCCESS_ROLE_EDITOR() {
        mock_findUser_SUCCESS();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setRole("ROLE_EDITOR");

        User user = new User();
        BeanUtils.copyProperties(updateUserRequest,user );
        Mockito.when(userRepository.save(any())).thenReturn(user);
        User updatedUser = userService.updateUser("email@email", updateUserRequest);

        assertEquals(updatedUser.getRole(), "ROLE_EDITOR");
    }

    @Test
    public void updateUser_SUCCESS_INVALID_ROLE() {
        mock_findUser_SUCCESS();
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setRole("ROLE_INVALID");
        User user = new User();
        BeanUtils.copyProperties(updateUserRequest,user );
        assertThrows(ResourceNotFoundException.class, ()-> userService.updateUser("email@email", updateUserRequest));
    }

    @Test
    public void deleteUser_SUCCESS(){
        mock_findUser_SUCCESS();
        userService.deleteUser("email@email");
    }

}