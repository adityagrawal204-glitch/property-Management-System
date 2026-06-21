package com.example.propertymgmt.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a logged-in account (property owner/landlord).
 * Each User owns zero or more Properties - see the @OneToMany below.
 */
@Entity
@Table(name = "app_user") // "user" is a reserved word in some SQL dialects, so we avoid it
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @JsonIgnore // never send the password hash back in any API response
    @Column(nullable = false)
    private String password;

    private String fullName;

    /**
     * The "owning" side of this relationship is Property (it has the foreign key).
     * mappedBy = "owner" means: "look at the `owner` field over in the Property class
     * to figure out this relationship" - User itself gets no extra DB column for this.
     */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // avoid huge nested JSON / infinite loops when serializing a User
    private List<Property> properties = new ArrayList<>();

    public User() {}

    public User(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public List<Property> getProperties() { return properties; }
    public void setProperties(List<Property> properties) { this.properties = properties; }
}
