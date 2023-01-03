package gov.tech.gtbookclub.controller;

import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.model.request.UpdateUserRequest;
import gov.tech.gtbookclub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    public ResponseEntity<User> get(){
        return new ResponseEntity<User>(userService.readUser(), HttpStatus.OK);
    }


    @PutMapping("/profile/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> update(@PathVariable String email, @RequestBody UpdateUserRequest updateUserRequest){
        return new ResponseEntity<User>(userService.updateUser(email, updateUserRequest), HttpStatus.OK);
    }

    @DeleteMapping("/deactivate/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> delete(@PathVariable String email){
        userService.deleteUser(email);
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }
}
