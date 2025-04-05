package com.tryriot.cryptography.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

@ExtendWith(MockitoExtension.class)
public class Base64EncodingServiceTest {

    @InjectMocks
    private Base64EncodingService base64EncodingService;

    @Nested
    class EncryptUnitTest {
        private final String flatJson = "{\"name\":\"Julien\",\"age\":30}";
        private final String nestedJson = "{\"user\":{\"email\":\"julien@example.com\"}}";

        @Test
        public void encrypt_payload_with_flat_json() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode input = mapper.readTree(flatJson);
            JsonNode result = base64EncodingService.encryptPayload(input);

            assertEquals("SnVsaWVu", result.get("name").asText());
            assertEquals("MzA=", result.get("age").asText());
        }

        @Test
        public void encrypt_payload_with_nested_json() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode input = mapper.readTree(nestedJson);
            JsonNode result = base64EncodingService.encryptPayload(input);

            assertTrue(result.has("user"));
            String encoded = result.get("user").asText();
            String decoded = new String(Base64.getDecoder().decode(encoded));
            JsonNode decodedNode = mapper.readTree(decoded);
            assertEquals("julien@example.com", decodedNode.get("email").asText());
        }
    }

    @Nested
    class DecryptUnitTest {
        @Test
        public void decrypt_payload_with_flat_encoded_json() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode input = mapper.readTree("{\"name\":\"SnVsaWVu\",\"age\":\"MzA=\"}");
            JsonNode result = base64EncodingService.decryptPayload(input);

            assertEquals("Julien", result.get("name").asText());
            assertEquals("30", result.get("age").asText());
        }

        @Test
        public void decrypt_payload_with_nested_encoded_json() throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            String encodedNested = Base64.getEncoder().encodeToString("{\"email\":\"julien@example.com\"}".getBytes());
            JsonNode input = mapper.readTree("{\"user\":\"" + encodedNested + "\"}");
            JsonNode result = base64EncodingService.decryptPayload(input);

            assertTrue(result.has("user"));
            JsonNode userNode = result.get("user");
            assertEquals("julien@example.com", userNode.get("email").asText());
        }
    }
}
