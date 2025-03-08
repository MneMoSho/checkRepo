package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.FlightCompanyDto;
import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.FlightCompany;
import com.example.checkrepo.entities.User;
import com.example.checkrepo.mapper.FlightCompanyMapper;
import com.example.checkrepo.repository.FlightCompanyRepository;
import com.example.checkrepo.repository.FlightRep;
import com.example.checkrepo.services.FlightCompanyService;
import lombok.AllArgsConstructor;
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
        System.out.println(flightCompanyDto.getCompanyName());
        flightCompanyRepository.save(FlightCompanyMapper.toEntity(flightCompanyDto));
    }

    @Override
    public Optional<FlightCompanyDto> addFlightToCompany(Long flightId, Long flightCompanyId) {
        Optional<FlightCompany> newFlightCompany = flightCompanyRepository.findById(flightCompanyId);
        newFlightCompany.get().getFlights().add(flightRepository.findById(flightId).get());
        FlightCompany newFlightComp = newFlightCompany.get();
        Flight newFlight = flightRepository.findById(flightId).get();
        newFlight.setFlightCompany(newFlightComp);
        flightRepository.save(newFlight);
        return newFlightCompany.map(FlightCompanyMapper::toFlightCompanyDto);
    }

    @Override
    public List<FlightCompanyDto> showAll() {
        return FlightCompanyMapper.toDtoList(flightCompanyRepository.findAll());
    }

    @Override
    public void deleteCompany(Long id) {
        ;
        System.out.println(id);
        //for (Flight sourceFlight : flightRepository.findAll()) {

        for (FlightCompany companySource : flightCompanyRepository.findAll()) {
            if (companySource.getId().equals(id)) {
                for(Flight flightSource : companySource.getFlights()) {
                    for(User userSource : flightSource.getUsers()) {
                        userSource.getFlights().remove(flightSource);
                        flightSource.getUsers().remove(userSource);
                    }
                    companySource.getFlights().remove(flightSource);
                    flightRepository.deleteById(flightSource.getId());
                }
                flightCompanyRepository.deleteById(id);
            }
        }
    }

   // @Override
   // public List<FlightDto> getByQueryParam(String companyName, Long destLength) {
   //
   // }
}

