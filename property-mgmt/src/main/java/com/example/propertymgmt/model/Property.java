package com.example.propertymgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A Property is a building/address owned by a User (the landlord).
 * It contains one or more Units (apartments, rooms, offices, etc).
 */
@Entity
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;        // e.g. "Sunset Apartments"
    private String addressLine; // street address
    private String city;
    private String state;
    private String zipCode;

    /**
     * Many Properties can belong to one User.
     * @ManyToOne is the "owning" side - this table gets the foreign key column (owner_id).
     * @JoinColumn names that foreign key column explicitly.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnore // don't serialize the full owner object back in every Property response
    private User owner;

    /**
     * One Property has many Units.
     * mappedBy = "property" points to the field in Unit that owns this relationship.
     * cascade = ALL means: delete a Property -> its Units get deleted too.
     */
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Unit> units = new ArrayList<>();

    public Property() {}

    public Property(String name, String addressLine, String city, String state, String zipCode, User owner) {
        this.name = name;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public List<Unit> getUnits() { return units; }
    public void setUnits(List<Unit> units) { this.units = units; }
}
