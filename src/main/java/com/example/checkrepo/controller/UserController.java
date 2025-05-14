package com.example.checkrepo.controller;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.service.impl.UserServiceImpl;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://checkrepo-1-1mf3.onrender.com")
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

    @PostMapping("/byFlight")
    public List<UserDto> byRoute(@RequestBody List<String> finalDests) {
        return userService.findByEndDest(finalDests);
    }

    @PostMapping("/fromExisting")
    public UserDto findExistingUser(@RequestBody UserDto checkUser) {
        return userService.findExistingUser(checkUser);
    }

    @PostMapping("/getAllUserFlights")
    public List<FlightDto> findAllUserFlights(@RequestBody UserDto user) {
        return userService.findUserFlights(user);
    }

    @PostMapping("/removeUser")
    public UserDto detachUser(Long flightId, @RequestBody UserDto user) {
        System.out.println("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");
    return userService.detachFlightFromUser(flightId, user);
    }

    @PostMapping("/saveFlightToUser")
    public void bookFlightToUser(Long flightId, @RequestBody UserDto user) {
      userService.bookFlightToUser(user, flightId);
    }
}
