package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.services.impl.FlightServiceImpl;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/flight")

public class FlightController {
    private  FlightServiceImpl flightService;

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

    @GET
    @GetMapping("/findByRoute")
    public List<FlightDto> getByRoute(@QueryParam("startDestination") String startDestination,
                                      @QueryParam("endDestination") String endDestination) {
        return flightService.findByRoute(startDestination, endDestination);
    }

    @DeleteMapping("/deleteById{id}")
    public List<FlightDto> deleteById(@PathParam("id") int id) {
        return flightService.deleteFlight(id);
    }

    @PostMapping("/addFlight")
    public void addNewFlight(@RequestBody FlightDto flightAdd) {
        //flightService.addNewFlight(flightAdd);
        flightService.createDbFlight(flightAdd);
    }
}
