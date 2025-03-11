package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.FlightCompany;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.FlightCompanyMapper;
import com.example.checkrepo.repository.FlightCompanyRepository;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.service.FlightCompanyService;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightCompanyServiceImpl implements FlightCompanyService {
    private final FlightCompanyRepository flightCompanyRepository;
    private final FlightRep flightRepository;

    @Override
    public void addFlightCompany(FlightCompanyDto flightCompanyDto) {
        flightCompanyRepository.save(FlightCompanyMapper.toEntity(flightCompanyDto));
    }

    @Override
    public Optional<FlightCompanyDto> addFlightToCompany(Long flightId, Long flightCompanyId) {
        FlightCompany newFlightCompany = flightCompanyRepository.findById(flightCompanyId)
                .orElseThrow(() -> new ObjectNotFoundException("Company cannot be found"));
        newFlightCompany.getFlights().add(flightRepository.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found")));
        Flight newFlight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found"));
        newFlight.setFlightCompany(newFlightCompany);
        flightRepository.save(newFlight);
        return Optional.ofNullable(FlightCompanyMapper.toFlightCompanyDto(newFlightCompany));
    }

    @Override
    public List<FlightCompanyDto> showAll() {
        if (flightCompanyRepository.findAll().isEmpty()) {
            throw new ObjectNotFoundException("Company List is empty search for something else ");
        }
        return FlightCompanyMapper.toDtoList(flightCompanyRepository.findAll());
    }

    @Override
    public void deleteCompany(Long companyId) {
        FlightCompany sourceFlightCompany = flightCompanyRepository.findById(companyId).orElseThrow(() -> new ObjectNotFoundException("Not found"));
        for (Flight flightSource : sourceFlightCompany.getFlights()) {
            for (User userSource : flightSource.getUsers()) {
                userSource.getFlights().remove(flightSource);
                flightSource.getUsers().remove(userSource);
            }
            sourceFlightCompany.getFlights().remove(flightSource);
            flightRepository.deleteById(flightSource.getId());
        }
       flightCompanyRepository.deleteById(companyId);
    }
}

