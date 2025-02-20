package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.services.FlightService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/flight")
public class FlightController {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightDto> addNewFlight(@RequestBody FlightDto flightDto) {
        FlightDto newFlight = flightService.createFlight(flightDto);
        return new ResponseEntity<>(newFlight, HttpStatus.CREATED);
    }
}
