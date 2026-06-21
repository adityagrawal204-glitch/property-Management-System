package com.example.propertymgmt.model;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * A Tenant represents the current occupant of a Unit.
 * This is the "owning" side of the one-to-one relationship with Unit -
 * it holds the foreign key (unit_id) - which is why @JoinColumn is here
 * and mappedBy is over on Unit instead.
 */
@Entity
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;
    private String phone;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, unique = true)
    private Unit unit;

    public Tenant() {}

    public Tenant(String fullName, String email, String phone,
                  LocalDate leaseStartDate, LocalDate leaseEndDate, Unit unit) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.leaseStartDate = leaseStartDate;
        this.leaseEndDate = leaseEndDate;
        this.unit = unit;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getLeaseStartDate() { return leaseStartDate; }
    public void setLeaseStartDate(LocalDate leaseStartDate) { this.leaseStartDate = leaseStartDate; }

    public LocalDate getLeaseEndDate() { return leaseEndDate; }
    public void setLeaseEndDate(LocalDate leaseEndDate) { this.leaseEndDate = leaseEndDate; }

    public Unit getUnit() { return unit; }
    public void setUnit(Unit unit) { this.unit = unit; }
}
