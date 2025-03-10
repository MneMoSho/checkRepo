package com.example.checkrepo.service;

import com.example.checkrepo.dto.FlightCompanyDto;

import java.util.List;
import java.util.Optional;

public interface FlightCompanyService {

    void addFlightCompany(FlightCompanyDto flightCompanyDto);

    Optional<FlightCompanyDto> addFlightToCompany(Long flightId, Long flightCompanyId);

    List<FlightCompanyDto> showAll();

    void deleteCompany(Long id);
}
