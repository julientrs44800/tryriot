package com.tryriot.cryptography.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tryriot.cryptography.exception.CreateSignatureFailureException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HMACSignatureServiceTest {
    private final HMACSignatureService signatureService = new HMACSignatureService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Nested
    class HMACSignatureTests {
        @Test
        public void produces_expected_signature() throws Exception {
            // Given
            String input = "{\"message\": \"Hello World\", \"timestamp\": 1616161616}";
            JsonNode jsonNode = mapper.readTree(input);

            // When
            String signature = signatureService.signPayload(jsonNode);

            // Then
            assertEquals("a619183f6a4c7a13c1cb9893bd06a5bedb5c9d53763635f5e2655cb9a0d990b6", signature);
        }

        @Test
        public void order_invariant() throws Exception {
            // Given
            String json1 = "{\"message\": \"Hello World\", \"timestamp\": 1616161616}";
            String json2 = "{\"timestamp\": 1616161616, \"message\": \"Hello World\"}";

            JsonNode node1 = mapper.readTree(json1);
            JsonNode node2 = mapper.readTree(json2);

            // When
            String sig1 = signatureService.signPayload(node1);
            String sig2 = signatureService.signPayload(node2);

            // Then
            assertEquals(sig1, sig2);
        }
    }

    @Nested
    class HMACSignatureErrorTests {
        @Test
        public void throws_exception_for_null_payload() {
            assertThrows(CreateSignatureFailureException.class, () -> {
                signatureService.signPayload(null);
            });
        }
    }

    @Nested
    class HMACSignatureVerificationTests {
        @Test
        public void returns_false_for_missing_signature() throws Exception {
            // Given
            String data = "{\"message\": \"Hello World\", \"timestamp\": 1616161616}";
            String wrapper = String.format("{\"data\": %s}", data);

            JsonNode payload = mapper.readTree(wrapper);

            // When Then
            assertFalse(signatureService.verifySignature(payload));
        }

        @Test
        public void returns_false_for_missing_data() throws Exception {
            // Given
            String signature = "a619183f6a4c7a13c1cb9893bd06a5bedb5c9d53763635f5e2655cb9a0d990b6";
            String wrapper = String.format("{\"signature\": \"%s\"}", signature);

            JsonNode payload = mapper.readTree(wrapper);

            // When Then
            assertFalse(signatureService.verifySignature(payload));
        }

        @Test
        public void returns_true_for_correct_signature() throws Exception {
            // Given
            String data = "{\"message\": \"Hello World\", \"timestamp\": 1616161616}";
            String signature = "a619183f6a4c7a13c1cb9893bd06a5bedb5c9d53763635f5e2655cb9a0d990b6";
            String wrapper = String.format("{\"data\": %s, \"signature\": \"%s\"}", data, signature);

            JsonNode payload = mapper.readTree(wrapper);

            // When Then
            assertTrue(signatureService.verifySignature(payload));
        }

        @Test
        public void returns_false_for_invalid_signature() throws Exception {
            // Given
            String data = "{\"message\": \"Hello World\", \"timestamp\": 1616161616}";
            String invalidSignature = "invalidsignature==";
            String wrapper = String.format("{\"data\": %s, \"signature\": \"%s\"}", data, invalidSignature);

            JsonNode payload = mapper.readTree(wrapper);

            // When Then
            assertFalse(signatureService.verifySignature(payload));
        }
    }
}
