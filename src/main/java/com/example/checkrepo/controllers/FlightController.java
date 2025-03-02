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

   // @GetMapping("/displayFlights")
   // public List<FlightDto> displayAllFlights() {
   //     return flightService.getList();
   // }
//
   // @GetMapping("/{id}")
   // public FlightDto getById(@PathParam("id") int id) {
   //     return flightService.findById(id);
   // }
//
   // @GET
   // @GetMapping("/findByEndDestination")
   // public List<FlightDto> getByName(@QueryParam("endDestination") String endDestination) {
   //     return flightService.findByName(endDestination);
   // }
//
   // @GET
   // @GetMapping("/findByRoute")
   // public List<FlightDto> getByRoute(@QueryParam("startDestination") String startDestination,
   //                                   @QueryParam("endDestination") String endDestination) {
   //     return flightService.findByRoute(startDestination, endDestination);
   // }
//
   // @DeleteMapping("/deleteById{id}")
   // public List<FlightDto> deleteById(@PathParam("id") int id) {
   //     return flightService.deleteFlight(id);
   // }

    @PostMapping("/addFlight")
    public void addNewFlight(@RequestBody FlightDto flightAdd) {
        flightService.createDbFlight(flightAdd);
    }

    @PostMapping("/newUser")
    public void newUser(@RequestBody UserDto userAdd) {
        userService.createUser(userAdd);
    }

    @GetMapping("/addingNewFlight")
        public UserDto addingNewFlight(@QueryParam("flightId") int flightId, @QueryParam("userId") int userId) {
        return userService.addingNewFlight(flightId, userId);
        }

    @GetMapping("/{id}")
    public Optional<FlightDto> getById(@PathParam("id") int id) {
        System.out.println(id);
        return flightService.findById(id);
    }

    @GetMapping("/DisplayUser")
    public UserDto displayUser(@QueryParam("Id") int Id) {
        return userService.getUserById(Id);
    }

   // @GetMapping("/DisplayFlight")
   // public FlightDto displayFlight(@RequestParam("Id") int Id) {
   //     return flightService.getFlightById(Id);
   // }
}
