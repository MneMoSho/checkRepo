package com.example.checkrepo.controller;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.service.impl.CompanyServiceImpl;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@AllArgsConstructor
public class CompanyController {
    private CompanyServiceImpl companyService;

    @PostMapping()
    void newCompany(@RequestBody CompanyDto companyDto) {
        companyService.addFlightCompany(companyDto);
    }

    @PutMapping("/assignFlight")
    public Optional<CompanyDto> addFlightToCompany(
           @QueryParam("flightId") Long flightId,
           @QueryParam("companyId") Long companyId) {
        return companyService.addFlightToCompany(flightId, companyId);
    }

    @GetMapping()
    public List<CompanyDto> displayAll() {
        return companyService.showAll();
    }

    @DeleteMapping("/{companiesId}")
    public void deleteById(@PathVariable Long companiesId) {
        companyService.deleteCompany(companiesId);
    }

    @GetMapping("/findByFlightNative")
    public List<FlightDto> findByCompanyIdNative(@RequestParam("companyId") Long companyId) {
        return companyService.getCompanyFlightsNative(companyId);
    }

    @GetMapping("/findByFlightJPQL")
    public List<FlightDto> findByCompanyIdJPQL(@RequestParam("companyId") Long companyId) {
        return companyService.getCompanyFlightsJPQL(companyId);
    }
}
