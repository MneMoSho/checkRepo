package com.example.checkrepo.service;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.dto.FlightDto;
import java.util.List;
import java.util.Optional;

public interface CompanyService {

    void addFlightCompany(CompanyDto companyDto);

    Optional<CompanyDto> addFlightToCompany(Long flightId, Long flightCompanyId);

    List<CompanyDto> showAll();

    void deleteCompany(Long id);

    List<CompanyDto> getCompanyFlightsNative(String destinationName);

    List<CompanyDto> getCompanyFlightsJpql(String destinationName);

    CompanyDto findById(Long companyId);
}
