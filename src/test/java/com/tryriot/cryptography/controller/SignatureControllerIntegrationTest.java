package com.tryriot.cryptography.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tryriot.config.integration.AbstractIntegrationTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SignatureControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    public void sign_and_verify_returns_true_for_valid_signature() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree("{\"message\": \"Hello World\", \"timestamp\": 1616161616}");
        JsonNode inputInOtherOrder = mapper.readTree("{\"timestamp\": 1616161616, \"message\": \"Hello World\"}");

        // When
        JsonNode responseSignature = performPostRequestExpectedSuccess("/sign", input, JsonNode.class);

        ObjectNode verifyInput = mapper.createObjectNode();
        verifyInput.set("data", inputInOtherOrder);
        verifyInput.set("signature", responseSignature.get("signature"));

        // Then
        mockMvc.perform(
                post("/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(verifyInput)))
                .andExpect(status().isNoContent()).andReturn();
    }

    @Test
    public void sign_and_verify_returns_false_for_invalid_signature() throws Exception {
        // Given
        ObjectMapper mapper = new ObjectMapper();
        JsonNode input = mapper.readTree("{\"message\": \"Hello World\", \"timestamp\": 1616161616}");

        ObjectNode verifyInput = mapper.createObjectNode();
        verifyInput.set("data", input);
        verifyInput.put("signature", "invalidsignature");

        // Then
        mockMvc.perform(
                        post("/verify")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(verifyInput)))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
