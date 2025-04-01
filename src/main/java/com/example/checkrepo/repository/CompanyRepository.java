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

    @Query("SELECT f FROM Flight f JOIN f.company c \n"
            + "WHERE f.startDestination = :destinationName")
    List<Flight> findByCompanyIdNative(@Param("destinationName") String destinationName);

    @Query(value = "SELECT f.id, length, startdestination, enddestination, companies_id "
            + "from companies c join flights f ON c.id = f.companies_id"
            + " where f.startdestination =:destinationName", nativeQuery = true)
    List<Flight> findByCompanyIdJpql(@Param("destinationName") String destinationName);
}
