package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.services.impl.FlightServiceImpl;
import com.example.checkrepo.services.impl.UserServiceImpl;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/flight")

public class FlightController {
    private final FlightServiceImpl flightService;
    private final UserServiceImpl userService;

    @PostMapping("/addFlight")
    public void addNewFlight(@RequestBody FlightDto flightAdd) {
        flightService.createDbFlight(flightAdd);
    }

    @PostMapping("/newUser")
    public void newUser(@RequestBody UserDto userAdd) {
        userService.createUser(userAdd);
    }

    @GetMapping("/{id}")
    public Optional<FlightDto> getById(@PathParam("id") Long id) {
        return flightService.findById(id);
    }

    @GetMapping("/addingNewFlight")
    public void addingNewFlight(@QueryParam("flightId") Long flightId, @QueryParam("userId") Long userId) {
        userService.addingNewFlight(flightId, userId);
    }

    @GetMapping("/DisplayUser")
    public Optional<UserDto> displayUser(@QueryParam("Id") Long Id) {
        return userService.getUserById(Id);
    }
}
