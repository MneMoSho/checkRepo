package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Flight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRep extends JpaRepository<Flight, Long> {

    @Query("SELECT m from Flight m where m.startDestination =:startDestination")
    List<Flight> findByStartDestinationJPQL(@Param("startDestination") String start);

    @Query(value = "SELECT * from flights m where m.startDestination =:startDestination", nativeQuery = true)
    List<Flight> findByStartDestinationNative(@Param("startDestination") String start);
}


