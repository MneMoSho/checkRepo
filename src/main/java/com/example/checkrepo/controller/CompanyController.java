package com.example.checkrepo.controller;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.service.impl.CompanyServiceImpl;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    public List<CompanyDto> findByCompanyIdNative(@RequestParam("endDestination")
                                                      String endDestination) {
        return companyService.getCompanyFlightsNative(endDestination);
    }

    @GetMapping("/findByFlightJPQL")
    public List<CompanyDto> findByCompanyIdJpql(@RequestParam("satrtDestinationName")
                                                    String endDest) {
        return companyService.getCompanyFlightsJpql(endDest);
    }

}
