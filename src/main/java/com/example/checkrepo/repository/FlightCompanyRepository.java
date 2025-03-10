package com.example.checkrepo.repository;

import com.example.checkrepo.entities.FlightCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightCompanyRepository extends JpaRepository<FlightCompany, Long> {}
