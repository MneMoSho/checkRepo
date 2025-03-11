package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.entities.Company;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.CompanyMapper;
import com.example.checkrepo.repository.CompanyRepository;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.service.CompanyService;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final FlightRep flightRepository;

    @Override
    public void addFlightCompany(CompanyDto companyDto) {
        System.out.println(companyDto.getCompanyName());
        companyRepository.save(CompanyMapper.toEntity(companyDto));
    }

    @Override
    public Optional<CompanyDto> addFlightToCompany(Long flightId, Long companyId) {
        Company newCompany = companyRepository.findById(companyId)
                .orElseThrow(() -> new ObjectNotFoundException("Company cannot be found"));
        newCompany.getFlights().add(flightRepository.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found")));
        Flight newFlight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ObjectNotFoundException("Flight cannot be found"));
        newFlight.setCompany(newCompany);
        flightRepository.save(newFlight);
        return Optional.ofNullable(CompanyMapper.toCompanyDto(newCompany));
    }

    @Override
    public List<CompanyDto> showAll() {
        if (companyRepository.findAll().isEmpty()) {
            throw new ObjectNotFoundException("Company List is empty search for something else ");
        }
        return CompanyMapper.toDtoList(companyRepository.findAll());
    }

    @Override
    public void deleteCompany(Long companyId) {
        Company sourceCompany = companyRepository
                .findById(companyId).orElseThrow(() -> new ObjectNotFoundException("Not found"));
        for (Flight flightSource : sourceCompany.getFlights()) {
            for (User userSource : flightSource.getUsers()) {
                userSource.getFlights().remove(flightSource);
            }
            flightRepository.delete(flightSource);
        }
        companyRepository.delete(sourceCompany);
    }
}

