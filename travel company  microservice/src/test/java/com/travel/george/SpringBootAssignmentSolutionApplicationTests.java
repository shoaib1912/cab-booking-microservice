package com.travel.george;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.travel.george.controller.CabBookingController;
import com.travel.george.controller.PassengerController;
import com.travel.george.model.CabBooking;
import com.travel.george.model.Passenger;
import com.travel.george.service.CabBookingService;
import com.travel.george.service.FareCalculatorService;
import com.travel.george.service.PassengerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JUnit 5 Unit Tests for Travel Company Microservices — Task 7
 *
 * Tests cover:
 *  1. Application context loads
 *  2. Controller auto-wiring (matches reference output)
 *  3. FareCalculatorService — fare logic for all cab types
 *  4. CabBookingService — book, get, cancel, delete
 *  5. PassengerService — add, get, update, delete
 *  6. REST controller response codes
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SpringBootAssignmentSolutionApplicationTests {

    // ─── Auto-wired beans (matches reference screenshot) ─────────────────────

    @Autowired
    private CabBookingController controller;

    @Autowired
    private PassengerController passengerController;

    @Autowired
    private CabBookingService cabBookingService;

    @Autowired
    private FareCalculatorService fareCalculatorService;

    @Autowired
    private PassengerService passengerService;

    // ─── Test 1: Context Loads ────────────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("Application context loads successfully")
    void contextLoads() {
        assertThat(controller).isNotNull();
        assertThat(passengerController).isNotNull();
        assertThat(cabBookingService).isNotNull();
        assertThat(fareCalculatorService).isNotNull();
        assertThat(passengerService).isNotNull();
    }

    // ─── Test 2: Fare Calculator — Economy ───────────────────────────────────

    @Test
    @Order(2)
    @DisplayName("Fare: Economy cab (type 1) at ₹10/km for 10 km = ₹100")
    void testFareEconomy() {
        double fare = fareCalculatorService.calculateFare(10.0, 1);
        assertEquals(100.0, fare, 0.01,
                "Economy fare for 10 km should be ₹100");
    }

    // ─── Test 3: Fare Calculator — Premium ───────────────────────────────────

    @Test
    @Order(3)
    @DisplayName("Fare: Premium cab (type 2) at ₹15/km for 10 km = ₹150")
    void testFarePremium() {
        double fare = fareCalculatorService.calculateFare(10.0, 2);
        assertEquals(150.0, fare, 0.01,
                "Premium fare for 10 km should be ₹150");
    }

    // ─── Test 4: Fare Calculator — Luxury ────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("Fare: Luxury cab (type 3) at ₹25/km for 10 km = ₹250")
    void testFareLuxury() {
        double fare = fareCalculatorService.calculateFare(10.0, 3);
        assertEquals(250.0, fare, 0.01,
                "Luxury fare for 10 km should be ₹250");
    }

    // ─── Test 5: Fare Calculator — Base Fare ─────────────────────────────────

    @Test
    @Order(5)
    @DisplayName("Fare: Short trip applies minimum base fare of ₹50")
    void testBaseFare() {
        // 1 km × ₹10 = ₹10, but base fare kicks in → ₹50
        double fare = fareCalculatorService.calculateFare(1.0, 1);
        assertEquals(FareCalculatorService.BASE_FARE, fare, 0.01,
                "Fare should not go below the base fare of ₹50");
    }

    // ─── Test 6: Fare Calculator — Invalid Cab Type ───────────────────────────

    @Test
    @Order(6)
    @DisplayName("Fare: Invalid cab type throws IllegalArgumentException")
    void testInvalidCabType() {
        assertThrows(IllegalArgumentException.class,
                () -> fareCalculatorService.calculateFare(10.0, 99),
                "Invalid cab type should throw IllegalArgumentException");
    }

    // ─── Test 7: Fare Calculator — Invalid Distance ───────────────────────────

    @Test
    @Order(7)
    @DisplayName("Fare: Zero distance throws IllegalArgumentException")
    void testZeroDistance() {
        assertThrows(IllegalArgumentException.class,
                () -> fareCalculatorService.calculateFare(0.0, 1),
                "Zero distance should throw IllegalArgumentException");
    }

    // ─── Test 8: Fare Calculator — Cab Type Names ────────────────────────────

    @Test
    @Order(8)
    @DisplayName("Fare: getCabTypeName returns correct names")
    void testCabTypeNames() {
        assertEquals("Economy", fareCalculatorService.getCabTypeName(1));
        assertEquals("Premium", fareCalculatorService.getCabTypeName(2));
        assertEquals("Luxury",  fareCalculatorService.getCabTypeName(3));
        assertEquals("Unknown", fareCalculatorService.getCabTypeName(0));
    }

    // ─── Test 9: Cab Booking — Book a Cab ────────────────────────────────────

    @Test
    @Order(9)
    @DisplayName("Service: bookCab() saves booking and auto-calculates fare")
    void testBookCab() {
        CabBooking booking = new CabBooking(
                "Home", "Office", 2, 12.0, "Alice");

        CabBooking saved = cabBookingService.bookCab(booking);

        assertNotNull(saved.getBookingId(), "Booking ID should be generated");
        assertEquals("Home", saved.getFrom());
        assertEquals("Office", saved.getTo());
        assertEquals("CONFIRMED", saved.getStatus());
        // 12 km × ₹15 (Premium) = ₹180
        assertEquals(180.0, saved.getFare(), 0.01,
                "Fare for 12 km Premium should be ₹180");
    }

    // ─── Test 10: Cab Booking — Get All Bookings ─────────────────────────────

    @Test
    @Order(10)
    @DisplayName("Service: getAllBookings() returns non-null list")
    void testGetAllBookings() {
        List<CabBooking> bookings = cabBookingService.getAllBookings();
        assertNotNull(bookings, "Bookings list should not be null");
    }

    // ─── Test 11: Cab Booking — Get By ID ────────────────────────────────────

    @Test
    @Order(11)
    @DisplayName("Service: getBookingById() returns correct booking")
    void testGetBookingById() {
        // Create a booking first
        CabBooking booking = new CabBooking("Park", "Mall", 1, 5.0, "Bob");
        CabBooking saved = cabBookingService.bookCab(booking);

        Optional<CabBooking> found = cabBookingService.getBookingById(saved.getBookingId());
        assertTrue(found.isPresent(), "Booking should be found by ID");
        assertEquals("Bob", found.get().getPassengerName());
    }

    // ─── Test 12: Cab Booking — Cancel ───────────────────────────────────────

    @Test
    @Order(12)
    @DisplayName("Service: cancelBooking() sets status to CANCELLED")
    void testCancelBooking() {
        CabBooking booking = new CabBooking("Station", "Hotel", 3, 8.0, "Carol");
        CabBooking saved = cabBookingService.bookCab(booking);

        Optional<CabBooking> cancelled = cabBookingService.cancelBooking(saved.getBookingId());
        assertTrue(cancelled.isPresent(), "Cancelled booking should be returned");
        assertEquals("CANCELLED", cancelled.get().getStatus());
    }

    // ─── Test 13: Cab Booking — Non-existent Cancel ───────────────────────────

    @Test
    @Order(13)
    @DisplayName("Service: cancelBooking() returns empty for unknown ID")
    void testCancelNonExistentBooking() {
        Optional<CabBooking> result = cabBookingService.cancelBooking(9999L);
        assertFalse(result.isPresent(), "Cancelling unknown ID should return empty");
    }

    // ─── Test 14: Cab Booking — Delete ───────────────────────────────────────

    @Test
    @Order(14)
    @DisplayName("Service: deleteBooking() returns true for existing booking")
    void testDeleteBooking() {
        CabBooking booking = new CabBooking("Airport", "City", 2, 20.0, "Dave");
        CabBooking saved = cabBookingService.bookCab(booking);
        boolean deleted = cabBookingService.deleteBooking(saved.getBookingId());
        assertTrue(deleted, "Existing booking should be deleted");
    }

    // ─── Test 15: Passenger — Add Passenger ──────────────────────────────────

    @Test
    @Order(15)
    @DisplayName("Service: addPassenger() saves and returns passenger with ID")
    void testAddPassenger() {
        Passenger passenger = new Passenger(
                "Eve Adams", "eve@test.com", "9876543210", "123 Main St", "GOLD");
        Passenger saved = passengerService.addPassenger(passenger);

        assertNotNull(saved.getPassengerId(), "Passenger ID should be generated");
        assertEquals("Eve Adams", saved.getName());
        assertEquals("GOLD", saved.getMembershipType());
    }

    // ─── Test 16: Passenger — Get All ────────────────────────────────────────

    @Test
    @Order(16)
    @DisplayName("Service: getAllPassengers() returns non-null list")
    void testGetAllPassengers() {
        List<Passenger> passengers = passengerService.getAllPassengers();
        assertNotNull(passengers, "Passengers list should not be null");
    }

    // ─── Test 17: Passenger — Get By ID ──────────────────────────────────────

    @Test
    @Order(17)
    @DisplayName("Service: getPassengerById() returns correct passenger")
    void testGetPassengerById() {
        Passenger passenger = new Passenger(
                "Frank Lee", "frank@test.com", "9001112222", "456 Oak Ave", "SILVER");
        Passenger saved = passengerService.addPassenger(passenger);

        Optional<Passenger> found = passengerService.getPassengerById(saved.getPassengerId());
        assertTrue(found.isPresent());
        assertEquals("Frank Lee", found.get().getName());
    }

    // ─── Test 18: Passenger — Update ─────────────────────────────────────────

    @Test
    @Order(18)
    @DisplayName("Service: updatePassenger() updates passenger details")
    void testUpdatePassenger() {
        Passenger passenger = new Passenger(
                "Grace Ho", "grace@test.com", "8887776666", "789 Pine Rd", "REGULAR");
        Passenger saved = passengerService.addPassenger(passenger);

        Passenger updated = new Passenger(
                "Grace Ho Updated", "grace.new@test.com", "8887776666", "789 Pine Rd", "GOLD");
        Optional<Passenger> result = passengerService.updatePassenger(saved.getPassengerId(), updated);

        assertTrue(result.isPresent());
        assertEquals("Grace Ho Updated", result.get().getName());
        assertEquals("GOLD", result.get().getMembershipType());
    }

    // ─── Test 19: Passenger — Delete ─────────────────────────────────────────

    @Test
    @Order(19)
    @DisplayName("Service: deletePassenger() returns true for existing passenger")
    void testDeletePassenger() {
        Passenger passenger = new Passenger(
                "Hank Mo", "hank@test.com", "7776665555", "1 Sea St", "REGULAR");
        Passenger saved = passengerService.addPassenger(passenger);
        boolean deleted = passengerService.deletePassenger(saved.getPassengerId());
        assertTrue(deleted, "Passenger should be deleted");
    }

    // ─── Test 20: Controller — GET /cab/fare ─────────────────────────────────

    @Test
    @Order(20)
    @DisplayName("Controller: GET /cab/fare returns 200 OK with fare details")
    void testFareEndpoint() {
        ResponseEntity<Map<String, Object>> response =
                controller.calculateFare(15.0, 2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(225.0, (Double) response.getBody().get("calculatedFare"), 0.01);
        assertEquals("Premium", response.getBody().get("cabTypeName"));
    }

    // ─── Test 21: Controller — POST /cab/book ────────────────────────────────

    @Test
    @Order(21)
    @DisplayName("Controller: POST /cab/book returns 201 Created")
    void testBookCabEndpoint() {
        CabBooking booking = new CabBooking("Gate 1", "Terminal 3", 1, 7.0, "Ivan");
        ResponseEntity<CabBooking> response = controller.bookCab(booking);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CONFIRMED", response.getBody().getStatus());
        // 7 km × ₹10 = ₹70 (above base fare)
        assertEquals(70.0, response.getBody().getFare(), 0.01);
    }

    // ─── Test 22: Controller — GET /cab/bookings ─────────────────────────────

    @Test
    @Order(22)
    @DisplayName("Controller: GET /cab/bookings returns 200 OK")
    void testGetAllBookingsEndpoint() {
        ResponseEntity<List<CabBooking>> response = controller.getAllBookings();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // ─── Test 23: Controller — GET /cab/booking/{id} not found ───────────────

    @Test
    @Order(23)
    @DisplayName("Controller: GET /cab/booking/9999 returns 404 Not Found")
    void testGetBookingByIdNotFound() {
        ResponseEntity<CabBooking> response = controller.getBookingById(9999L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    // ─── Test 24: Total Counts ────────────────────────────────────────────────

    @Test
    @Order(24)
    @DisplayName("Service: getTotalBookings() and getTotalPassengers() return positive counts")
    void testTotalCounts() {
        long bookings = cabBookingService.getTotalBookings();
        long passengers = passengerService.getTotalPassengers();
        assertTrue(bookings >= 0, "Total bookings should be non-negative");
        assertTrue(passengers >= 0, "Total passengers should be non-negative");
    }
}
