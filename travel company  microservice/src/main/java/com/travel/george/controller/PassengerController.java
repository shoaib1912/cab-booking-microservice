package com.travel.george.controller;

import com.travel.george.model.Passenger;
import com.travel.george.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Passenger Profile REST Controller — Task 4
 *
 * Exposes REST endpoints for managing passenger profiles.
 * Also serves the Thymeleaf passenger-profile page (Task 3 & 9).
 *
 * Base URL: /passenger
 */
@Controller
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // ─── HTML Page Endpoint (Thymeleaf) ──────────────────────────────────────

    /**
     * GET /passenger/profile-form
     * Serves the HTML page to add/view passenger profiles.
     */
    @GetMapping("/profile-form")
    public String showPassengerForm(Model model) {
        model.addAttribute("passenger", new Passenger());
        model.addAttribute("passengers", passengerService.getAllPassengers());
        return "passenger-profile";
    }

    // ─── REST API Endpoints ───────────────────────────────────────────────────

    /**
     * POST /passenger/add
     * Adds a new passenger profile.
     *
     * @param passenger JSON body with name, email, phone, address, membershipType
     * @return 201 Created with saved passenger
     */
    @PostMapping(value = "/add", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Passenger> addPassenger(@RequestBody Passenger passenger) {
        Passenger saved = passengerService.addPassenger(passenger);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * GET /passenger/all
     * Returns all passenger profiles.
     *
     * @return 200 OK with list of passengers
     */
    @GetMapping(value = "/all", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<Passenger>> getAllPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    /**
     * GET /passenger/{id}
     * Returns a passenger by ID.
     *
     * @param id passenger ID path variable
     * @return 200 OK with passenger, or 404 Not Found
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Passenger> getPassengerById(@PathVariable Long id) {
        Optional<Passenger> passenger = passengerService.getPassengerById(id);
        return passenger
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /passenger/email/{email}
     * Returns a passenger by email address.
     */
    @GetMapping(value = "/email/{email}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Passenger> getPassengerByEmail(@PathVariable String email) {
        Optional<Passenger> passenger = passengerService.getPassengerByEmail(email);
        return passenger
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /passenger/{id}
     * Updates an existing passenger's profile.
     *
     * @param id          passenger ID
     * @param updatedData updated passenger JSON body
     * @return 200 OK with updated passenger, or 404 Not Found
     */
    @PutMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Passenger> updatePassenger(
            @PathVariable Long id,
            @RequestBody Passenger updatedData) {
        Optional<Passenger> updated = passengerService.updatePassenger(id, updatedData);
        return updated
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /passenger/{id}
     * Deletes a passenger profile.
     *
     * @param id passenger ID
     * @return 200 OK on success, 404 Not Found if missing
     */
    @DeleteMapping(value = "/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deletePassenger(@PathVariable Long id) {
        boolean deleted = passengerService.deletePassenger(id);
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Passenger " + id + " deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Passenger " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /passenger/count
     * Returns total number of registered passengers.
     */
    @GetMapping(value = "/count", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getTotalPassengers() {
        Map<String, Long> result = new HashMap<>();
        result.put("totalPassengers", passengerService.getTotalPassengers());
        return ResponseEntity.ok(result);
    }
}
