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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
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

    @GetMapping("/start")
    public List<FlightDto> getBySameStart(@QueryParam("startingPoint") String flightStart) {
        return flightService.getByStartDest(flightStart);
    }

    @GetMapping("/search")
    public List<FlightDto> selectByParameters(@QueryParam("companyName") String companyName,
                                               @QueryParam("maxLength") Long maxLength) {
        return flightService.getByQueryParam(companyName, maxLength);
    }

    @GetMapping("/fromExcel")
    public void getFromExcel() throws IOException {
        flightService.getFromExcel();
    }
}
