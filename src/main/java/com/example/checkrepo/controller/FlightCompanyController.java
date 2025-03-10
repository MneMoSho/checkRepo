package com.example.checkrepo.controller;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.service.impl.FlightCompanyServiceImpl;
import jakarta.ws.rs.QueryParam;
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

@RestController
@RequestMapping("/api/company")
@AllArgsConstructor
public class FlightCompanyController {
    private FlightCompanyServiceImpl flightCompanyService;

    @PostMapping("/companies")
    void newCompany(@RequestBody FlightCompanyDto flightCompanyDto) {
        flightCompanyService.addFlightCompany(flightCompanyDto);
    }

    @PutMapping("/assignFlight")
    public Optional<FlightCompanyDto> addFlightToCompany(
           @QueryParam("flightId") Long flightId,
           @QueryParam("flightCompanyId") Long flightCompanyId) {
        return flightCompanyService.addFlightToCompany(flightId, flightCompanyId);
    }

    @GetMapping("/yAll")
    public List<FlightCompanyDto> displayAll() {
        return flightCompanyService.showAll();
    }

    @DeleteMapping("/{companyId}")
    public void deleteById(@PathVariable Long companyId) {
        flightCompanyService.deleteCompany(companyId);
    }
}
