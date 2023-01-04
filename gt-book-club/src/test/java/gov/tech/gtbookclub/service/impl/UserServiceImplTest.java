package gov.tech.gtbookclub.service.impl;

import gov.tech.gtbookclub.exception.ItemExistsException;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.CreateUserRequest;
import gov.tech.gtbookclub.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
    public void createUser_ItemExistsException(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("email@email");

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));
        assertThrows(ItemExistsException.class, ()-> userService.createUser(createUserRequest) );
    }

    public User mock_user(){
        return User.builder()
                .id("id1")
                .name("name1")
                .email("email@email1")
                .password("password1")
                .role("ROLE_USER")
                .build();
    }

    @Test
    public void createUser_SUCCESS(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("name");
        createUserRequest.setEmail("email@email");
        createUserRequest.setPassword("password");
        createUserRequest.setRole("ROLE_USER");

        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(any())).thenReturn(mock_user());

        User user = userService.createUser(createUserRequest);
        assertEquals( user.getName(), "name1");
        assertEquals( user.getEmail(), "email@email1");
        assertEquals( user.getPassword(), "password1");
        assertEquals( user.getRole(), "ROLE_USER");
    }

}