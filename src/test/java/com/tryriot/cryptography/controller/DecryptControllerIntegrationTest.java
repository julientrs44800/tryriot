package com.tryriot.cryptography.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryriot.config.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DecryptControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    public void encrypt_and_decrypt_return_the_original_payload_not_nested_json() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree( "{\"name\":\"Julien\",\"age\":30}");
        JsonNode responseEncrypted = performPostRequestExpectedSuccess("/encrypt", input, JsonNode.class);

        // When
        JsonNode responseDecrypted = performPostRequestExpectedSuccess("/decrypt", responseEncrypted, JsonNode.class);

        // Then
        assertNotNull(responseDecrypted);
        assertEquals(responseDecrypted.get("name").asText(), "Julien");
        assertEquals(responseDecrypted.get("age").asText(), "30");
    }

    @Test
    public void encrypt_and_decrypt_return_the_original_payload_nested_json() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree( "{\n  \"name\": \"John Doe\",\n  \"age\": 30,\n  \"contact\": {\n    \"email\": \"john@example.com\",\n    \"phone\": \"123-456-7890\"\n  }\n}");
        JsonNode responseEncrypted = performPostRequestExpectedSuccess("/encrypt", input, JsonNode.class);

        // When
        JsonNode responseDecrypted = performPostRequestExpectedSuccess("/decrypt", responseEncrypted, JsonNode.class);

        // Then
        assertNotNull(responseDecrypted);
        assertEquals(responseDecrypted.get("name").asText(), "John Doe");
        assertEquals(responseDecrypted.get("age").asText(), "30");
        assertEquals(responseDecrypted.get("contact").get("phone").asText(), "123-456-7890");
        assertEquals(responseDecrypted.get("contact").get("email").asText(), "john@example.com");
    }
}
