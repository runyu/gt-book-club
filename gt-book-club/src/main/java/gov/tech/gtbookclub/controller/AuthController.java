package gov.tech.gtbookclub.controller;

import gov.tech.gtbookclub.model.dto.AuthModel;
import gov.tech.gtbookclub.model.dto.UserModel;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> save(@Valid @RequestBody UserModel user) {
        return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthModel authModel) throws Exception {

        return new ResponseEntity<String>("user logged in", HttpStatus.OK);
    }
}
