package com.travel.george.service;

import org.springframework.stereotype.Service;

/**
 * Fare Calculator Microservice — Task 6
 *
 * Calculates the cab fare based on distance and cab type.
 *
 * Fare structure:
 *   Type 1 — Economy : ₹10 per km
 *   Type 2 — Premium : ₹15 per km
 *   Type 3 — Luxury  : ₹25 per km
 */
@Service
public class FareCalculatorService {

    // Rate constants (₹ per km)
    public static final double ECONOMY_RATE  = 10.0;
    public static final double PREMIUM_RATE  = 15.0;
    public static final double LUXURY_RATE   = 25.0;

    // Minimum base fare (₹)
    public static final double BASE_FARE = 50.0;

    /**
     * Calculates total fare for a trip.
     *
     * @param distanceKm distance of the trip in kilometres
     * @param typeOfCab  1 = Economy, 2 = Premium, 3 = Luxury
     * @return calculated fare in ₹ (never below BASE_FARE)
     * @throws IllegalArgumentException if typeOfCab is invalid or distance <= 0
     */
    public double calculateFare(double distanceKm, int typeOfCab) {
        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0 km.");
        }

        double ratePerKm = getRatePerKm(typeOfCab);
        double calculatedFare = distanceKm * ratePerKm;

        // Apply base fare (minimum charge)
        return Math.max(calculatedFare, BASE_FARE);
    }

    /**
     * Returns the rate per km based on cab type.
     *
     * @param typeOfCab  1, 2, or 3
     * @return rate per km in ₹
     */
    public double getRatePerKm(int typeOfCab) {
        switch (typeOfCab) {
            case 1: return ECONOMY_RATE;
            case 2: return PREMIUM_RATE;
            case 3: return LUXURY_RATE;
            default:
                throw new IllegalArgumentException(
                        "Invalid cab type: " + typeOfCab +
                        ". Valid types are 1 (Economy), 2 (Premium), 3 (Luxury).");
        }
    }

    /**
     * Returns the human-readable name of the cab type.
     *
     * @param typeOfCab  1, 2, or 3
     * @return cab type name string
     */
    public String getCabTypeName(int typeOfCab) {
        switch (typeOfCab) {
            case 1: return "Economy";
            case 2: return "Premium";
            case 3: return "Luxury";
            default: return "Unknown";
        }
    }
}
