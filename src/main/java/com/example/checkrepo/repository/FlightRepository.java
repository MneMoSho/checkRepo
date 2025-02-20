package com.example.checkrepo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.checkrepo.entities.Flight;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {}
