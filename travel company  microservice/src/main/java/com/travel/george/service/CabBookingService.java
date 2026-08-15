package com.travel.george.service;

import com.travel.george.model.CabBooking;
import com.travel.george.repository.CabBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Cab Booking Microservice — Task 5
 *
 * Handles all business logic for creating, retrieving,
 * and cancelling cab bookings.
 */
@Service
public class CabBookingService {

    @Autowired
    private CabBookingRepository cabBookingRepository;

    @Autowired
    private FareCalculatorService fareCalculatorService;

    // ─── Book a Cab ──────────────────────────────────────────────────────────

    /**
     * Books a cab and persists it to the database.
     * Fare is automatically calculated and stored.
     *
     * @param booking the CabBooking object with trip details
     * @return saved CabBooking with generated ID and fare
     */
    public CabBooking bookCab(CabBooking booking) {
        // Calculate and set fare before saving
        double fare = fareCalculatorService.calculateFare(
                booking.getDistanceKm(), booking.getTypeOfCab());
        booking.setFare(fare);
        booking.setStatus("CONFIRMED");

        return cabBookingRepository.save(booking);
    }

    // ─── Retrieve Bookings ───────────────────────────────────────────────────

    /**
     * Returns all bookings in the system.
     */
    public List<CabBooking> getAllBookings() {
        return cabBookingRepository.findAll();
    }

    /**
     * Finds a booking by its ID.
     *
     * @param bookingId the booking ID
     * @return Optional containing the booking if found
     */
    public Optional<CabBooking> getBookingById(Long bookingId) {
        return cabBookingRepository.findById(bookingId);
    }

    /**
     * Returns all bookings for a given passenger name.
     */
    public List<CabBooking> getBookingsByPassenger(String passengerName) {
        return cabBookingRepository.findByPassengerName(passengerName);
    }

    /**
     * Returns all bookings with the given status.
     */
    public List<CabBooking> getBookingsByStatus(String status) {
        return cabBookingRepository.findByStatus(status);
    }

    // ─── Cancel Booking ──────────────────────────────────────────────────────

    /**
     * Cancels a booking by setting its status to CANCELLED.
     *
     * @param bookingId the booking ID to cancel
     * @return updated booking, or empty if not found
     */
    public Optional<CabBooking> cancelBooking(Long bookingId) {
        Optional<CabBooking> optionalBooking = cabBookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            CabBooking booking = optionalBooking.get();
            booking.setStatus("CANCELLED");
            cabBookingRepository.save(booking);
            return Optional.of(booking);
        }
        return Optional.empty();
    }

    // ─── Delete Booking ──────────────────────────────────────────────────────

    /**
     * Permanently deletes a booking by ID.
     *
     * @param bookingId the booking ID to delete
     * @return true if deleted, false if not found
     */
    public boolean deleteBooking(Long bookingId) {
        if (cabBookingRepository.existsById(bookingId)) {
            cabBookingRepository.deleteById(bookingId);
            return true;
        }
        return false;
    }

    /**
     * Returns total number of bookings.
     */
    public long getTotalBookings() {
        return cabBookingRepository.count();
    }
}
