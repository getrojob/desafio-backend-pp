package com.picpaysimplificado.controller;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.userDTO;
import com.picpaysimplificado.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody userDTO user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{document}")
    public ResponseEntity<User> getUserByDocument(@RequestParam String doc) throws Exception {
        User user = userService.findUserByDocument(doc);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
