package com.travel.george.model;

import jakarta.persistence.*;

/**
 * Entity representing a Passenger profile.
 * Task 4 — Passenger Profile Data
 */
@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    /** Membership type: REGULAR, SILVER, GOLD */
    @Column(nullable = false)
    private String membershipType;

    // ─── Constructors ────────────────────────────────────────────────────────

    public Passenger() {
        this.membershipType = "REGULAR";
    }

    public Passenger(String name, String email, String phone,
                     String address, String membershipType) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.membershipType = membershipType;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMembershipType() { return membershipType; }
    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }

    @Override
    public String toString() {
        return "Passenger{" +
                "passengerId=" + passengerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", membershipType='" + membershipType + '\'' +
                '}';
    }
}
