package com.example.checkrepo.repository;

import com.example.checkrepo.entities.Company;
import com.example.checkrepo.entities.Flight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("Select f from Flight f left join f.company c where c.id = :companyId ")
    List<Flight> findByCompanyIdNative(@Param("companyId") Long companyId);

    @Query(value = "SELECT flights.length, flights.enddestination, flights.startdestination, "
            + "flights.id, flights.companies_id from companies left join flights "
            + "ON companies.id = flights.companies_id where companies_id = 2", nativeQuery = true)
    List<Flight> findByCompanyIdJpql(@Param("companyId") Long companyId);
}
