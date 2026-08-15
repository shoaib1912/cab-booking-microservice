package com.travel.george.repository;

import com.travel.george.model.CabBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CabBooking entity — extends JpaRepository
 * providing all standard CRUD operations.
 */
@Repository
public interface CabBookingRepository extends JpaRepository<CabBooking, Long> {

    /** Find all bookings for a specific passenger */
    List<CabBooking> findByPassengerName(String passengerName);

    /** Find all bookings with a given status (CONFIRMED / CANCELLED) */
    List<CabBooking> findByStatus(String status);

    /** Find all bookings for a specific cab type */
    List<CabBooking> findByTypeOfCab(int typeOfCab);
}
