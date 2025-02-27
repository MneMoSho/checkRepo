package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRep extends JpaRepository<Flight, Integer> {}

