package com.example.checkrepo.controller;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.service.impl.FlightServiceImpl;
import com.example.checkrepo.service.impl.UserServiceImpl;
import jakarta.ws.rs.QueryParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/flights")

public class FlightController {
    private final FlightServiceImpl flightService;
    private final UserServiceImpl userService;

    @PostMapping()
    public void addNewFlight(@RequestBody FlightDto flightAdd) {
        flightService.createDbFlight(flightAdd);
    }

    @GetMapping("/{id}")
    public Optional<FlightDto> getById(@PathVariable Long id) {
        return flightService.findById(id);
    }

    @GetMapping()
    public List<FlightDto> displayAllFlight() {
        return flightService.displayAll();
    }

    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
    }

    @PutMapping("/assignFlightToUser")
    public UserDto addingNewFlight(@QueryParam("flightId") Long flightId,
                                   @QueryParam("userId") Long userId) {
        return userService.addingNewFlight(flightId, userId);
    }

    @GetMapping("/startNative")
    public List<FlightDto> getBySameStartNative(@QueryParam("startingPoint") String flightStart) {
        return flightService.getByStartDestNative(flightStart);
    }

    @GetMapping("/startJPQL")
    public List<FlightDto> getBySameStartJpql(@QueryParam("startingPoint") String flightStart) {
        return flightService.getByStartDestJpql(flightStart);
    }

    @PostMapping("/bulkOp")
    public List<FlightDto> bulkOperation(@RequestBody List<String> nameOfCompanies) {
        return flightService.bulkOperation(nameOfCompanies);
    }

    @PostMapping("/addMany")
    public List<FlightDto> saveFlights(@RequestBody List<FlightDto> nameOfFlights) {
        return flightService.postFlights(nameOfFlights);
    }

    @PostMapping("/FindFromFront")
    public List<FlightDto> findFromFront(@RequestBody FlightDto destination) {
        return flightService.findFromFront(destination);
    }

    @GetMapping("/findMinPrice")
    public int findMinPrice() {
        return flightService.findMinPrice();
    }

    @GetMapping("/findByCountry")
    public List<FlightDto> findByCountry() {
        return flightService.findByCountry();
    }
}
