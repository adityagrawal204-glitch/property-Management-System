package com.example.propertymgmt;

import com.example.propertymgmt.dto.AuthDtos.SignupRequest;
import com.example.propertymgmt.dto.PropertyDtos.PropertyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests using MockMvc - these spin up the full Spring context
 * and simulate real HTTP requests, without needing a separately running server.
 *
 * Covers the most important end-to-end flow: signup -> get a token ->
 * use the token to access a protected endpoint -> confirm an
 * unauthenticated request to the same endpoint is rejected.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PropertyMgmtApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signup_returnsTokenAndUserInfo() throws Exception {
        SignupRequest request = new SignupRequest();
        request.setFullName("Test Owner");
        request.setEmail("owner1@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value("owner1@example.com"));
    }

    @Test
    void accessingPropertiesWithoutToken_isRejected() throws Exception {
        mockMvc.perform(get("/api/properties"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createAndFetchProperty_withValidToken_succeeds() throws Exception {
        // 1. Sign up a fresh user and grab their JWT
        SignupRequest signup = new SignupRequest();
        signup.setFullName("Test Owner 2");
        signup.setEmail("owner2@example.com");
        signup.setPassword("password123");

        String signupResponse = mockMvc.perform(post("/api/auth/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signup)))
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(signupResponse).get("token").asText();

        // 2. Use the token to create a property
        PropertyRequest property = new PropertyRequest();
        property.setName("Test Towers");
        property.setAddressLine("123 Main St");
        property.setCity("Austin");
        property.setState("TX");
        property.setZipCode("78701");

        mockMvc.perform(post("/api/properties")
                        .header("Authorization", "Bearer " + token)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(property)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Towers"));

        // 3. Confirm it shows up when listing properties for this user
        mockMvc.perform(get("/api/properties")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Austin"));
    }
}
