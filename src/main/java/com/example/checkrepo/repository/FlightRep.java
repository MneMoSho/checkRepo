package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Flight;
import com.example.checkrepo.entities.FlightCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRep extends JpaRepository<Flight, Long> {

    @Query("SELECT m from Flight m where m.flightCompany.companyName =:flightCompany AND m.length< :maxLength")
    List<Flight> findByCompany(@Param("flightCompany") String flightCompany, @Param("maxLength") Long maxLength);

}

