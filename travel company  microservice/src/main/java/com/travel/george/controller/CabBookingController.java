package com.travel.george.controller;

import com.travel.george.model.CabBooking;
import com.travel.george.service.CabBookingService;
import com.travel.george.service.FareCalculatorService;
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
 * Cab Booking REST Controller — Task 4 & 5
 *
 * Exposes REST endpoints for the Cab Booking Microservice.
 * Also serves the Thymeleaf HTML booking page (Task 3 & 9).
 *
 * Base URL: /cab
 */
@Controller
@RequestMapping("/cab")
public class CabBookingController {

    @Autowired
    private CabBookingService cabBookingService;

    @Autowired
    private FareCalculatorService fareCalculatorService;

    // ─── HTML Page Endpoints (Thymeleaf) ─────────────────────────────────────

    /**
     * GET /cab/book-form
     * Serves the HTML form to book a cab (Task 3).
     */
    @GetMapping("/book-form")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new CabBooking());
        return "book-cab";
    }

    /**
     * GET /cab/view-bookings
     * Serves the HTML page showing all bookings.
     */
    @GetMapping("/view-bookings")
    public String showAllBookings(Model model) {
        List<CabBooking> bookings = cabBookingService.getAllBookings();
        model.addAttribute("bookings", bookings);

        // Pre-calculate stats for the template (Thymeleaf doesn't support Java stream lambdas)
        long confirmedCount = bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getStatus())).count();
        long cancelledCount = bookings.stream()
                .filter(b -> "CANCELLED".equals(b.getStatus())).count();
        double totalRevenue = bookings.stream()
                .mapToDouble(CabBooking::getFare).sum();

        model.addAttribute("confirmedCount", confirmedCount);
        model.addAttribute("cancelledCount", cancelledCount);
        model.addAttribute("totalRevenue", String.format("%.2f", totalRevenue));
        model.addAttribute("totalCount", bookings.size());

        return "view-bookings";
    }

    // ─── REST API Endpoints ───────────────────────────────────────────────────

    /**
     * POST /cab/book
     * Books a cab and returns the booking with calculated fare.
     *
     * @param booking  JSON body with from, to, typeOfCab, distanceKm, passengerName
     * @return 201 Created with the saved booking
     */
    @PostMapping(value = "/book", produces = "application/json")
    @ResponseBody
    public ResponseEntity<CabBooking> bookCab(@RequestBody CabBooking booking) {
        CabBooking savedBooking = cabBookingService.bookCab(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    /**
     * GET /cab/bookings
     * Returns a list of all bookings in the system.
     *
     * @return 200 OK with list of bookings
     */
    @GetMapping(value = "/bookings", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<CabBooking>> getAllBookings() {
        List<CabBooking> bookings = cabBookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    /**
     * GET /cab/booking/{id}
     * Returns a single booking by its ID.
     *
     * @param id booking ID path variable
     * @return 200 OK with booking, or 404 Not Found
     */
    @GetMapping(value = "/booking/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<CabBooking> getBookingById(@PathVariable Long id) {
        Optional<CabBooking> booking = cabBookingService.getBookingById(id);
        return booking
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /cab/bookings/passenger/{name}
     * Returns all bookings for a specific passenger.
     */
    @GetMapping(value = "/bookings/passenger/{name}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<CabBooking>> getBookingsByPassenger(@PathVariable String name) {
        List<CabBooking> bookings = cabBookingService.getBookingsByPassenger(name);
        return ResponseEntity.ok(bookings);
    }

    /**
     * PUT /cab/booking/{id}/cancel
     * Cancels a booking by marking its status as CANCELLED.
     *
     * @param id booking ID path variable
     * @return 200 OK with updated booking, or 404 Not Found
     */
    @PutMapping(value = "/booking/{id}/cancel", produces = "application/json")
    @ResponseBody
    public ResponseEntity<CabBooking> cancelBooking(@PathVariable Long id) {
        Optional<CabBooking> cancelled = cabBookingService.cancelBooking(id);
        return cancelled
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /cab/booking/{id}
     * Permanently deletes a booking.
     *
     * @param id booking ID path variable
     * @return 200 OK on success, 404 Not Found if missing
     */
    @DeleteMapping(value = "/booking/{id}", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteBooking(@PathVariable Long id) {
        boolean deleted = cabBookingService.deleteBooking(id);
        Map<String, String> response = new HashMap<>();
        if (deleted) {
            response.put("message", "Booking " + id + " deleted successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Booking " + id + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * GET /cab/fare
     * Calculates fare for given distance and cab type (Task 6 — Fare Microservice).
     *
     * @param distance  distance in km (request param)
     * @param type      cab type: 1, 2, or 3 (request param)
     * @return 200 OK with fare details as JSON
     */
    @GetMapping(value = "/fare", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> calculateFare(
            @RequestParam double distance,
            @RequestParam int type) {

        double fare = fareCalculatorService.calculateFare(distance, type);
        String cabTypeName = fareCalculatorService.getCabTypeName(type);
        double ratePerKm = fareCalculatorService.getRatePerKm(type);

        Map<String, Object> fareDetails = new HashMap<>();
        fareDetails.put("distanceKm", distance);
        fareDetails.put("cabType", type);
        fareDetails.put("cabTypeName", cabTypeName);
        fareDetails.put("ratePerKm", ratePerKm);
        fareDetails.put("calculatedFare", fare);
        fareDetails.put("currency", "INR");

        return ResponseEntity.ok(fareDetails);
    }

    /**
     * GET /cab/count
     * Returns total number of bookings.
     */
    @GetMapping(value = "/count", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getTotalBookings() {
        Map<String, Long> result = new HashMap<>();
        result.put("totalBookings", cabBookingService.getTotalBookings());
        return ResponseEntity.ok(result);
    }
}
