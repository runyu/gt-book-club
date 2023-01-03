package gov.tech.gtbookclub.controller;

import gov.tech.gtbookclub.model.dto.AuthModel;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.CreateUserRequest;
import gov.tech.gtbookclub.model.response.JwtResponse;
import gov.tech.gtbookclub.security.CustomUserDetailsService;
import gov.tech.gtbookclub.service.UserService;
import gov.tech.gtbookclub.util.JwtTokenUtil;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> save(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return new ResponseEntity<User>(userService.createUser(createUserRequest), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthModel authModel) throws Exception {
        
        authenticate(authModel.getEmail(), authModel.getPassword());

        // we need to generate the jwt token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authModel.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
    }

    private void authenticate(String email, String password) throws AuthenticationException {
        try{
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (DisabledException e){
            throw new AuthenticationException("User Disabled");
        }catch (BadCredentialsException e){
            throw new AuthenticationException("Bad Credentials");
        }
    }
}
