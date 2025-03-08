package com.example.checkrepo.services;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.FlightCompany;

import java.util.List;
import java.util.Optional;

public interface FlightCompanyService {

    void addFlightCompany(FlightCompanyDto flightCompanyDto);

    Optional<FlightCompanyDto> addFlightToCompany(Long flightId, Long flightCompanyId);

    List<FlightCompanyDto> showAll();

    void deleteCompany(Long id);

  //  List<FlightDto> getByQueryParam(String companyName, Long destLength);
}
