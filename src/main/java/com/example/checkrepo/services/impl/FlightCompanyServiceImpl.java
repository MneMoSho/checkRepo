package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.FlightCompany;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.FlightCompanyMapper;
import com.example.checkrepo.repository.FlightCompanyRepository;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.services.FlightCompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.lang.invoke.SwitchPoint;
import java.util.List;
import java.util.Optional;

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
        FlightCompany newFlightCompany = flightCompanyRepository.findById(flightCompanyId).orElseThrow(() -> new ObjectNotFoundException("Company cannot be found"));
        newFlightCompany.getFlights().add(flightRepository.findById(flightId).orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found")));
        Flight newFlight = flightRepository.findById(flightId).orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found"));
        newFlight.setFlightCompany(newFlightCompany);
        flightRepository.save(newFlight);
        return Optional.ofNullable(FlightCompanyMapper.toFlightCompanyDto(newFlightCompany));
    }

    @Override
    public List<FlightCompanyDto> showAll() {
        if (flightCompanyRepository.findAll().isEmpty())
            throw new ObjectNotFoundException("Company cannot be found");
        return FlightCompanyMapper.toDtoList(flightCompanyRepository.findAll());
    }

    @Override
    public void deleteCompany(Long companyId) {
        for (FlightCompany companySource : flightCompanyRepository.findAll()) {
            FlightCompany sourceFlightCompany = flightCompanyRepository.findById(companyId).orElseThrow(() -> new ObjectNotFoundException("Company cannot be found"));
            for (Flight flightSource : sourceFlightCompany.getFlights()) {
                for (User userSource : flightSource.getUsers()) {
                    userSource.getFlights().remove(flightSource);
                    flightSource.getUsers().remove(userSource);
                }
                companySource.getFlights().remove(flightSource);
                flightRepository.deleteById(flightSource.getId());
            }
            flightCompanyRepository.deleteById(companyId);
        }

    }
}

