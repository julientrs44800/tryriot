package com.tryriot.cryptography.domain.port;

import com.fasterxml.jackson.databind.JsonNode;

public interface EncodingInterface {
    JsonNode encryptPayload(JsonNode decryptedPayload);

    JsonNode decryptPayload(JsonNode encryptedPayload);
}
