package com.travel.george.repository;

import com.travel.george.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Passenger entity — extends JpaRepository
 * providing all standard CRUD operations.
 */
@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

    /** Find a passenger by email address */
    Optional<Passenger> findByEmail(String email);

    /** Find passengers by membership type */
    java.util.List<Passenger> findByMembershipType(String membershipType);
}
