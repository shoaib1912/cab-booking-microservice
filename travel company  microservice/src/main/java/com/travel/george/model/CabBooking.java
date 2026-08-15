package com.travel.george.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a Cab Booking.
 * Task 5 — Microservice: Book the Cab
 */
@Entity
@Table(name = "cab_bookings")
public class CabBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;

    /** From location (pickup point) */
    @Column(name = "from_location", nullable = false)
    private String from;

    /** To location (drop-off point) */
    @Column(name = "to_location", nullable = false)
    private String to;

    /**
     * Type of cab:
     *   1 = Economy  (₹10/km)
     *   2 = Premium  (₹15/km)
     *   3 = Luxury   (₹25/km)
     */
    @Column(nullable = false)
    private int typeOfCab;

    /** Distance in km between pickup and drop-off */
    @Column(nullable = false)
    private double distanceKm;

    /** Calculated fare in ₹ */
    private double fare;

    /** Passenger associated with this booking */
    @Column(nullable = false)
    private String passengerName;

    /** Booking timestamp */
    @Column(nullable = false)
    private LocalDateTime bookingTime;

    /** Booking status: CONFIRMED, CANCELLED */
    @Column(nullable = false)
    private String status;

    // ─── Constructors ────────────────────────────────────────────────────────

    public CabBooking() {
        this.bookingTime = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    public CabBooking(String from, String to, int typeOfCab,
                      double distanceKm, String passengerName) {
        this.from = from;
        this.to = to;
        this.typeOfCab = typeOfCab;
        this.distanceKm = distanceKm;
        this.passengerName = passengerName;
        this.bookingTime = LocalDateTime.now();
        this.status = "CONFIRMED";
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public int getTypeOfCab() { return typeOfCab; }
    public void setTypeOfCab(int typeOfCab) { this.typeOfCab = typeOfCab; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public LocalDateTime getBookingTime() { return bookingTime; }
    public void setBookingTime(LocalDateTime bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "CabBooking{" +
                "bookingId=" + bookingId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", typeOfCab=" + typeOfCab +
                ", distanceKm=" + distanceKm +
                ", fare=" + fare +
                ", passengerName='" + passengerName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
