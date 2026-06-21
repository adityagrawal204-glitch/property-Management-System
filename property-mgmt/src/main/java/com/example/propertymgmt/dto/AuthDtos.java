package com.example.propertymgmt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    // Request body for POST /api/auth/signup
    public static class SignupRequest {
        @NotBlank
        private String fullName;

        @NotBlank @Email
        private String email;

        @NotBlank @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // Request body for POST /api/auth/login
    public static class LoginRequest {
        @NotBlank @Email
        private String email;

        @NotBlank
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // What we send back after a successful signup/login
    public static class AuthResponse {
        private String token;
        private String email;
        private String fullName;

        public AuthResponse(String token, String email, String fullName) {
            this.token = token;
            this.email = email;
            this.fullName = fullName;
        }

        public String getToken() { return token; }
        public String getEmail() { return email; }
        public String getFullName() { return fullName; }
    }
}
