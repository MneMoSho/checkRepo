package com.example.checkrepo.repository;

import com.example.checkrepo.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT username, email, u.id FROM users u "
            + "JOIN user_flights uf ON u.id = uf.user_id JOIN flights on uf.flight_id"
            + " = flights.id where startdestination = :startPoint", nativeQuery = true)
    List<User> findByDestNative(@Param("startPoint") String startPoint);

    @Query("SELECT u FROM User u JOIN u.flights uf "
            + "JOIN uf.users f WHERE uf.startDestination = :startPoint")
    List<User> findByDestJpql(@Param("startPoint") String startPoint);
}

