package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.services.dataLoader.FlightLoader;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Getter
@Repository
public class FlightRepository {
    private List<Flight> flights = new ArrayList<>();
    private final FlightLoader flightLoader = new FlightLoader();

    public void createNewFlight() {
        flights = flightLoader.createNewFlight(flights);
    }
    public Flight getById(final int id) {
        return flights.get(id);
    }
    public List<Flight> getByName(final String name) { //поставил final
        System.out.println("looking by last name");
        List<Flight> bufList = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getEndDestination().equals(name)) { //поставил final
                System.out.println(flight.getId());
                bufList.add(flight);
            }
        }
        return bufList;
    }
    public void addFlight(final Flight flight) { //поставил final
        flights.add(flight);
    }
    public List<Flight> deleteFlight(final int numberOfFlight) {
        flights.remove(numberOfFlight);
        return flights;
    }
}
