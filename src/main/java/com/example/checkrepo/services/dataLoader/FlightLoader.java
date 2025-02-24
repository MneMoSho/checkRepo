package com.example.checkrepo.services.dataLoader;

import com.example.checkrepo.entities.Flight;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FlightLoader {

    public List<Flight> createNewFlight(final List<Flight> listFlight) {
        Flight flight1 = new Flight(1, 234, "Beijing", "Moscow");
        Flight flight2 = new Flight(2, 456, "Moscow", "Shanghai");
        Flight flight3 = new Flight(3, 123, "Tokyo", "Shanghai");

        listFlight.add(flight1);
        listFlight.add(flight2);
        listFlight.add(flight3);
        return listFlight;
    }
}
