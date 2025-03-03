package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.entities.Flight;
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

        @GetMapping("/{id}")
    public Optional<FlightDto> getById(@PathVariable Long id) {
        return flightService.findById(id);
    }

    @PutMapping("/addingFlight")
    public UserDto addingNewFlight(@QueryParam("flightId") Long flightId, @QueryParam("userId") Long userId) {
       return userService.addingNewFlight(flightId, userId);
    }
}
