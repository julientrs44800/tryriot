package com.tryriot.cryptography.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tryriot.cryptography.exception.DecryptionFailureException;
import com.tryriot.cryptography.exception.EncryptionFailureException;
import com.tryriot.cryptography.domain.port.EncodingInterface;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class Base64EncodingService implements EncodingInterface {
    private void flattenAndEncode(JsonNode node, ObjectNode result) {
        node.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();

            String encoded;
            if (value.isValueNode()) {
                encoded = Base64.getEncoder().encodeToString(value.asText().getBytes());
            } else {
                encoded = Base64.getEncoder().encodeToString(value.toString().getBytes());
            }
            result.put(key, encoded);
        });
    }

    public JsonNode encryptPayload(JsonNode payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode flattenedNode = mapper.createObjectNode();
            flattenAndEncode(payload, flattenedNode);

            return flattenedNode;
        } catch (Exception e) {
            throw new EncryptionFailureException("Failed to encrypt flattened properties");
        }
    }

    public JsonNode decryptPayload(JsonNode payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode result = mapper.createObjectNode();

            payload.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                String base64Value = entry.getValue().asText();
                try {
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Value);
                    String decodedString = new String(decodedBytes);

                    try {
                        JsonNode nested = mapper.readTree(decodedString);
                        result.set(key, nested);
                    } catch (Exception ex) {
                        result.put(key, decodedString);
                    }
                } catch (IllegalArgumentException ex) {
                    result.put(key, base64Value);
                }
            });

            return result;
        } catch (Exception e) {
            throw new DecryptionFailureException("Failed to decrypt properties");
        }
    }
}
