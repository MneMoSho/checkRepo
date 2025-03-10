package com.example.checkrepo.controller;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.dto.UserDto;
import com.example.checkrepo.service.impl.FlightServiceImpl;
import com.example.checkrepo.service.impl.UserServiceImpl;
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

    @GetMapping("displayAll")
    public List<FlightDto> displayAllFlight() {
        return flightService.displayAll();
    }

    @DeleteMapping("/remove/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
    }

    @PutMapping("/addFlightToUser")
    public UserDto addingNewFlight(@QueryParam("flightId") Long flightId, @QueryParam("userId") Long userId) {
       return userService.addingNewFlight(flightId, userId);
    }

    @GetMapping("/getByStart")
    public List<FlightDto> getBySameStart(@QueryParam("startingPoint") String flightStart) {
        return flightService.getByStartDest(flightStart);
    }

    @GetMapping("/selectByParametres")
    public List <FlightDto> selectByParameters(@QueryParam("companyName") String companyName, @QueryParam("maxLength") Long maxLength) {

        if(maxLength == null) {
            System.out.println("TTTTTTTTTTTT");
        }

        return flightService.getByQueryParam(companyName, maxLength);
    }
}
