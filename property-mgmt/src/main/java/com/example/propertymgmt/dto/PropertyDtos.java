package com.example.propertymgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class PropertyDtos {

    public static class PropertyRequest {
        @NotBlank
        private String name;
        @NotBlank
        private String addressLine;
        @NotBlank
        private String city;
        private String state;
        private String zipCode;

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
    }

    public static class UnitRequest {
        @NotBlank
        private String unitNumber;
        @PositiveOrZero
        private int bedrooms;
        @PositiveOrZero
        private int bathrooms;
        @NotNull @PositiveOrZero
        private BigDecimal rentAmount;

        public String getUnitNumber() { return unitNumber; }
        public void setUnitNumber(String unitNumber) { this.unitNumber = unitNumber; }
        public int getBedrooms() { return bedrooms; }
        public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }
        public int getBathrooms() { return bathrooms; }
        public void setBathrooms(int bathrooms) { this.bathrooms = bathrooms; }
        public BigDecimal getRentAmount() { return rentAmount; }
        public void setRentAmount(BigDecimal rentAmount) { this.rentAmount = rentAmount; }
    }

    // Response shape for GET /api/dashboard/occupancy
    public static class OccupancyDashboard {
        private long totalUnits;
        private long occupiedUnits;
        private long vacantUnits;
        private double occupancyRate; // percentage, e.g. 72.5
        private BigDecimal totalMonthlyRentCollected; // sum of rent on OCCUPIED units only
        private BigDecimal potentialMonthlyRent;       // sum of rent on ALL units

        public OccupancyDashboard(long totalUnits, long occupiedUnits, BigDecimal totalMonthlyRentCollected,
                                   BigDecimal potentialMonthlyRent) {
            this.totalUnits = totalUnits;
            this.occupiedUnits = occupiedUnits;
            this.vacantUnits = totalUnits - occupiedUnits;
            this.occupancyRate = totalUnits == 0 ? 0.0 : (occupiedUnits * 100.0) / totalUnits;
            this.totalMonthlyRentCollected = totalMonthlyRentCollected;
            this.potentialMonthlyRent = potentialMonthlyRent;
        }

        public long getTotalUnits() { return totalUnits; }
        public long getOccupiedUnits() { return occupiedUnits; }
        public long getVacantUnits() { return vacantUnits; }
        public double getOccupancyRate() { return occupancyRate; }
        public BigDecimal getTotalMonthlyRentCollected() { return totalMonthlyRentCollected; }
        public BigDecimal getPotentialMonthlyRent() { return potentialMonthlyRent; }
    }
}
