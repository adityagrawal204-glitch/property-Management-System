package com.example.propertymgmt.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * A Unit is an individual rentable space within a Property
 * (e.g. "Apt 2B", "Suite 100"). This is where occupancy and rent
 * are actually tracked - a Property itself has no rent, its Units do.
 */
@Entity
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String unitNumber;   // e.g. "2B"
    private int bedrooms;
    private int bathrooms;
    private BigDecimal rentAmount;

    @Enumerated(EnumType.STRING) // store the enum as readable text ("VACANT") not a number
    private UnitStatus status = UnitStatus.VACANT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    /**
     * One Unit has at most one current Tenant.
     * mappedBy = "unit" - the Tenant entity owns the foreign key (unit_id).
     * This is a true one-to-one: a unit is either vacant or has exactly one tenant record.
     */
    @OneToOne(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Tenant tenant;

    public Unit() {}

    public Unit(String unitNumber, int bedrooms, int bathrooms, BigDecimal rentAmount, Property property) {
        this.unitNumber = unitNumber;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.rentAmount = rentAmount;
        this.property = property;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUnitNumber() { return unitNumber; }
    public void setUnitNumber(String unitNumber) { this.unitNumber = unitNumber; }

    public int getBedrooms() { return bedrooms; }
    public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

    public int getBathrooms() { return bathrooms; }
    public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }

    public BigDecimal getRentAmount() { return rentAmount; }
    public void setRentAmount(BigDecimal rentAmount) { this.rentAmount = rentAmount; }

    public UnitStatus getStatus() { return status; }
    public void setStatus(UnitStatus status) { this.status = status; }

    public Property getProperty() { return property; }
    public void setProperty(Property property) { this.property = property; }

    public Tenant getTenant() { return tenant; }
    public void setTenant(Tenant tenant) { this.tenant = tenant; }
}
