package gov.tech.gtbookclub.controller;

import gov.tech.gtbookclub.model.dto.UserModel;
import gov.tech.gtbookclub.model.entity.User;
import gov.tech.gtbookclub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> get(){
        return new ResponseEntity<User>(userService.readUser("du123@gmail.com"), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<User> update(@RequestBody UserModel user){
        return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<HttpStatus> delete(){
        userService.deleteUser("");
        return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
    }

}
