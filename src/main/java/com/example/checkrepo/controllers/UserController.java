package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.services.impl.UserServiceImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @PostMapping("/newUser")
    public void newUser(@RequestBody UserDto userAdd) {
        userService.createUser(userAdd);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> displayUser(@PathVariable Long id) {
        System.out.println(id);
        UserDto newUser = userService.getUserById(id);
        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
    }

    @GetMapping("/users")
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
