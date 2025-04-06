package com.tryriot.cryptography.domain.port;

import com.fasterxml.jackson.databind.JsonNode;

public interface SignatureInterface {
    String signPayload(JsonNode payload);
    boolean verifySignature(JsonNode payload);
}
