package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.services.Impl.FlightServiceImpl;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/flight")

public class FlightController {
    private final FlightServiceImpl flightService = new FlightServiceImpl();

    @GetMapping("/newFlights")
    public void createNewFlight() {
        flightService.createFlight();
    }

    @GetMapping("/displayFlights")
    public List<FlightDto> displayAllFlights() {
        return flightService.getList();
    }

    @GetMapping("/GetById{id}")
    public FlightDto getById(@PathParam("id") int id) {
        return flightService.findById(id);
    }

    @GET
    @GetMapping("/findByEndDestination")
    public List<FlightDto> getByName(@QueryParam("endDestination") String endDestination) {
        return flightService.findByName(endDestination);
    }

    @DeleteMapping("/deleteById{id}")
    public List<FlightDto> deleteById(@PathParam("id") int id) {
        return flightService.deleteFlight(id);
    }

    @PostMapping("/addFlight")
    public void addNewFlight(@RequestBody FlightDto flightAdd) {
        flightService.addNewFlight(flightAdd);
        System.out.println("added");
    }
}
