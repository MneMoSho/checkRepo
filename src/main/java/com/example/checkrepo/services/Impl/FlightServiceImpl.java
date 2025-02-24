package com.example.checkrepo.services.Impl;

import com.example.checkrepo.dto.FlightDto;
import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.mapper.FlightMapper;
import com.example.checkrepo.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FlightServiceImpl  {
    private final FlightRepository flightRepository = new FlightRepository();

    public void createFlight(){
        flightRepository.createNewFlight();
    }

    public FlightDto findById(int id){
        return FlightMapper.mapToFlightDto(flightRepository.getById(id));
    }

    public void addNewFlight(FlightDto flightAdd){
        Flight flightNew = FlightMapper.mapToFlight(flightAdd);
        flightRepository.addFlight(flightNew);
    }

    public List<FlightDto> findByName(String name){
        return FlightMapper.convertDto(flightRepository.getByName(name));
    }

   public List<FlightDto> getList(){
       return FlightMapper.convertDto(flightRepository.getFlights());
    }

    public List<FlightDto> deleteFlight(int id){
        return FlightMapper.convertDto(flightRepository.deleteFlight(id));
    }
}
