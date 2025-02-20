package com.example.checkrepo.controllers;

import com.example.checkrepo.dto.FlightDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flight")
public class FlightController {
    @Autowired
    private ObjectMapper objectMapper;

//    @GetMapping("/api/flight")
//    public String giveFlight() {
//        Flight flight = new Flight(123, 45, "Moscow");
//        String jsonData = null;
//        try {
//            jsonData = objectMapper.writeValueAsString(flight);
//        } catch (JsonProcessingException error) {
//            System.out.println(error);
//        }
//        return jsonData;
//    }

//    @GetMapping("/{flightId}")
//    public String valueFromQuery(@PathVariable String flightId){
//    }
}
