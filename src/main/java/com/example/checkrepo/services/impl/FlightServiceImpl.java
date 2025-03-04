package com.example.checkrepo.services.impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRep;

import java.util.Optional;

import com.example.checkrepo.services.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    //private final FlightRepository flightRepository = new FlightRepository();
    //private final FlightRepository flightRepository = new FlightRepository();
    private final FlightRep flightRep;

  //  @Override
  //  public Optional<FlightDto> findById(int id) {
  //     // return flightMapper.toFlightDto(flightRepository.getById(id));
  //     return flightMapper.toFlightDto(flightRep.findById(id));
  //  }
//
  //  public void addNewFlight(FlightDto flightAdd) {
  //      Flight flightNew = FlightMapper.mapToFlight(flightAdd);
  //      flightRepository.addFlight(flightNew);
  //  }
//
  //  public List<FlightDto> findByName(String name) {
  //      return FlightMapper.convertDto(flightRepository.getByName(name));
  //  }
//
  //  public List<FlightDto> getList() {
  //      return FlightMapper.convertDto(flightRepository.getFlights());
  //  }
//
  //  public List<FlightDto> deleteFlight(int id) {
  //      id -=1;
  //      return FlightMapper.convertDto(flightRepository.deleteFlight(id));
  //  }
  //  public List<FlightDto> findByRoute(String startDestination, String endDestination){
  //      return FlightMapper.convertDto(flightRepository.getByRoute(startDestination, endDestination));
  //  }

    @Override
    public void createDbFlight(FlightDto FlightDto) {
        flightRep.save(FlightMapper.toEntity(FlightDto));
    }

    @Override
    public Optional<FlightDto> findById(Long id) {
        Optional<Flight> optionalById = flightRep.findById(id);
        return optionalById.map(FlightMapper::toFlightDto);
    }

  //  @Override
  //  public FlightDto getFlightById(int id) {
  //      return flightMapper.toFlightDto(flightRep.findById(id).get());
  //  }
}
