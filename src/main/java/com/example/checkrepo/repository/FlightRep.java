package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Flight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRep extends JpaRepository<Flight, Long> {

    @Query("SELECT m from Flight m where m.flightCompany.companyName =:flightCompany "
            + "AND m.length< :maxLength")
    List<Flight> findByCompany(@Param("flightCompany") String flightCompany,
                               @Param("maxLength") Long maxLength);

    @Query("SELECT m from Flight m where m.startDestination =:startDestination")
    List<Flight> findByStartDestination(@Param("startDestination") String start);
}

