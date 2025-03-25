package com.example.checkrepo.controller;

import com.example.checkrepo.dto.UserDto;

import java.util.List;

import com.example.checkrepo.service.impl.UserServiceImpl;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserServiceImpl userService;

    @PostMapping()
    public void newUser(@RequestBody UserDto userAdd) {
        userService.createUser(userAdd);
    }

    @GetMapping("/{id}")
    public UserDto displayUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping()
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }

    @GetMapping("/byFlightNative")
    public List<UserDto> byFlightNative(@QueryParam("startDest") String startDest) {
        return userService.findByStartDestNative(startDest);
    }

    @GetMapping("/byFlightJPQL")
    public List<UserDto> byFlightJPQL(@QueryParam("startDest") String startDest) {
        return userService.findByStartDestJPQL(startDest);
    }
}
