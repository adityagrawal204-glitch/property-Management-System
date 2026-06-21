package com.example.propertymgmt.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class TenantDtos {

    public static class TenantRequest {
        @NotBlank
        private String fullName;
        private String email;
        private String phone;
        private LocalDate leaseStartDate;
        private LocalDate leaseEndDate;

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
    }
}
