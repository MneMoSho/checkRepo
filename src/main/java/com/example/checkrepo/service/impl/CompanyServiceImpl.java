package com.example.checkrepo.service.impl;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Company;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.exception.ObjectNotFoundException;
import com.example.checkrepo.mapper.CompanyMapper;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.CompanyRepository;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.service.CompanyService;
import com.example.checkrepo.service.cache.Cache;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final FlightRep flightRepository;
    private final Cache cache;

    @Override
    public void addFlightCompany(CompanyDto companyDto) {
        System.out.println(companyDto.getCompanyName());
        Company company = CompanyMapper.toEntity(companyDto);
        companyRepository.save(company);
        cache.putCompany(company.getId(), CompanyMapper.toCompanyDto(company));
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
        cache.updateCpompany(companyId, CompanyMapper.toCompanyDto(newCompany));
        return Optional.ofNullable(CompanyMapper.toCompanyDto(newCompany));
    }

    @Override
    public List<CompanyDto> showAll() {
        Collection<CompanyDto> allCompanies = cache.getAllCompanies();
        if (!allCompanies.isEmpty() && allCompanies.size() == flightRepository.count()) {
            return new ArrayList<>(allCompanies);
        } else {
            List<CompanyDto> companies = companyRepository.findAll()
                    .stream().map(CompanyMapper::toCompanyDto).toList();
            if (!allCompanies.isEmpty()) {
                for (CompanyDto source : companies) {
                    if (companies.stream().noneMatch(company -> company
                            .getCompanyId().equals(source.getCompanyId()))) {
                        cache.putCompany(source.getCompanyId(), source);
                    }
                }
            } else {
                companies.forEach(company -> cache.putCompany(company.getCompanyId(), company));
            }
            return companies;
        }
    }

    @Override
    public void deleteCompany(Long companyId) {
        Company sourceCompany = companyRepository
                .findById(companyId).orElseThrow(() -> new ObjectNotFoundException("Not found"));
        for (Flight flightSource : sourceCompany.getFlights()) {
            for (User userSource : flightSource.getUsers()) {
                userSource.getFlights().remove(flightSource);
            }
            cache.deleteFlight(flightSource.getId());
            flightRepository.delete(flightSource);
        }
        if (cache.getCompany(companyId) != null) {
            cache.deleteCompany(companyId);
        }
        companyRepository.delete(sourceCompany);
    }

    @Override
    public List<CompanyDto> getCompanyFlightsNative(String destinationName) {
        List<FlightDto> dtoList = FlightMapper
                .toDtoListShallow(companyRepository.findByCompanyIdNative(destinationName));
        Map<Long, CompanyDto> companyDtoMap = new HashMap<>();
        for (FlightDto flightDto : dtoList) {
            Long companyId = flightDto.getCompanyId();
            CompanyDto companyDto = companyDtoMap.computeIfAbsent(companyId, id -> new
                    CompanyDto(companyId, companyRepository.findById(companyId)
                    .get().getCompanyName(), new HashSet<>()));
            companyDto.getFlights().add(flightDto);
        }
        List<CompanyDto> company = new ArrayList<>(companyDtoMap.values());
        return company;
    }

    @Override
    public List<CompanyDto> getCompanyFlightsJpql(String destinationName) {
        List<FlightDto> dtoList = FlightMapper
                .toDtoListShallow(companyRepository.findByCompanyIdJpql(destinationName));
        Map<Long, CompanyDto> companyDtoMap = new HashMap<>();
        for (FlightDto flightDto : dtoList) {
            Long companyId = flightDto.getCompanyId();
            CompanyDto companyDto = companyDtoMap.computeIfAbsent(
                    companyId, id -> new CompanyDto(companyId,
                    companyRepository.findById(companyId).get().getCompanyName(), new HashSet<>()));
            companyDto.getFlights().add(flightDto);
        }
        List<CompanyDto> company = new ArrayList<>(companyDtoMap.values());
        return company;
    }
}

