package com.tryriot.cryptography.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryriot.config.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EncryptControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void should_returns_the_encrypted_payload_not_nested_input() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree( "{\"name\":\"Julien\",\"age\":30}");

        // When
        JsonNode response = performPostRequestExpectedSuccess("/encrypt", input, JsonNode.class);

        // Then
        assertNotNull(response);
        assertEquals(response.get("name").asText(), "SnVsaWVu");
        assertEquals(response.get("age").asText(), "MzA=");
    }

    @Test
    public void should_returns_the_encrypted_payload_nested_input() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree( "{\n  \"name\": \"John Doe\",\n  \"age\": 30,\n  \"contact\": {\n    \"email\": \"john@example.com\",\n    \"phone\": \"123-456-7890\"\n  }\n}");

        // When
        JsonNode response = performPostRequestExpectedSuccess("/encrypt", input, JsonNode.class);

        // Then
        assertNotNull(response);
        assertEquals(response.get("name").asText(), "Sm9obiBEb2U=");
        assertEquals(response.get("age").asText(), "MzA=");
        assertEquals(response.get("contact").asText(), "eyJlbWFpbCI6ImpvaG5AZXhhbXBsZS5jb20iLCJwaG9uZSI6IjEyMy00NTYtNzg5MCJ9");
    }
}
