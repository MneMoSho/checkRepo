package com.example.checkrepo.controller;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.service.impl.FlightCompanyServiceImpl;
import jakarta.ws.rs.QueryParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/company")
@AllArgsConstructor
public class FlightCompanyController {
    private FlightCompanyServiceImpl flightCompanyService;

    @PostMapping("/addNew")
    void NewCompany(@RequestBody FlightCompanyDto flightCompanyDto) {
        flightCompanyService.addFlightCompany(flightCompanyDto);
    }

    @PutMapping("/assign")
    public Optional<FlightCompanyDto> addFlightToCompany(@QueryParam("flightId") Long flightId, @QueryParam("flightCompanyId") Long flightCompanyId) {
       return flightCompanyService.addFlightToCompany(flightId, flightCompanyId);
    }

    @GetMapping("/displayAll")
    public List<FlightCompanyDto> displayAll() {
        return flightCompanyService.showAll();
    }

    @DeleteMapping("/{CompanyId}")
    public void deleteById(@PathVariable Long CompanyId) {
        flightCompanyService.deleteCompany(CompanyId);
    }
}
