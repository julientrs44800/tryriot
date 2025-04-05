package com.tryriot.cryptography.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.domain.application.DecryptPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tryriot.common.constants.CryptographyAlgorithm.BASE64;

@RestController
@RequiredArgsConstructor
public class DecryptController {
    public CryptographyAlgorithm DEFAULT_ALGORITHM = BASE64;

    private final DecryptPayload decryptPayload;

    @PostMapping("/decrypt")
    @Operation(
        summary = "Decrypt a JSON payload",
        responses = {
                @ApiResponse(responseCode = "200", description = "Payload decrypted successfully"),
                @ApiResponse(responseCode = "400", description = "Not a valid payload json")
        }
    )
    public ResponseEntity<JsonNode> decrypt(@RequestBody JsonNode payload) {
        JsonNode decryptedPayload = decryptPayload.handle(payload, DEFAULT_ALGORITHM);
        return ResponseEntity.ok(decryptedPayload);
    }
}
