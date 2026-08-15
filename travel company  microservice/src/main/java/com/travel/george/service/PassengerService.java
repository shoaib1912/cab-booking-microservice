package com.travel.george.service;

import com.travel.george.model.Passenger;
import com.travel.george.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Passenger Profile Service
 * Manages all CRUD operations for passenger profiles.
 */
@Service
public class PassengerService {

    @Autowired
    private PassengerRepository passengerRepository;

    /** Add a new passenger profile */
    public Passenger addPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    /** Get all passenger profiles */
    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    /** Get a passenger by ID */
    public Optional<Passenger> getPassengerById(Long id) {
        return passengerRepository.findById(id);
    }

    /** Get a passenger by email */
    public Optional<Passenger> getPassengerByEmail(String email) {
        return passengerRepository.findByEmail(email);
    }

    /** Update an existing passenger */
    public Optional<Passenger> updatePassenger(Long id, Passenger updatedData) {
        return passengerRepository.findById(id).map(existing -> {
            existing.setName(updatedData.getName());
            existing.setEmail(updatedData.getEmail());
            existing.setPhone(updatedData.getPhone());
            existing.setAddress(updatedData.getAddress());
            existing.setMembershipType(updatedData.getMembershipType());
            return passengerRepository.save(existing);
        });
    }

    /** Delete a passenger by ID */
    public boolean deletePassenger(Long id) {
        if (passengerRepository.existsById(id)) {
            passengerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /** Get passengers by membership type */
    public List<Passenger> getByMembershipType(String type) {
        return passengerRepository.findByMembershipType(type);
    }

    /** Total passenger count */
    public long getTotalPassengers() {
        return passengerRepository.count();
    }
}
