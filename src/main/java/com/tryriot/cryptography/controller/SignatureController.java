package com.tryriot.cryptography.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.controller.DTO.SignatureOutputDTO;
import com.tryriot.cryptography.domain.application.SignPayload;
import com.tryriot.cryptography.domain.application.VerifySignature;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.tryriot.common.constants.CryptographyAlgorithm.HMAC;

@RestController
@RequiredArgsConstructor
public class SignatureController {
    private  CryptographyAlgorithm DEFAULT_ALGORITHM = HMAC;

    private final SignPayload signPayload;

    private final VerifySignature verifySignature;

    @PostMapping("/sign")
    @Operation(
        summary = "Encrypt a JSON payload",
        responses = {
                @ApiResponse(responseCode = "200", description = "Payload sign successfully"),
                @ApiResponse(responseCode = "400", description = "Not a valid payload json")
        }
    )
    public ResponseEntity<SignatureOutputDTO> sign(@RequestBody JsonNode payload) {
        String signature = this.signPayload.handle(payload, DEFAULT_ALGORITHM);

        return ResponseEntity.ok(new SignatureOutputDTO(signature));
    }

    @PostMapping("/verify")
    @Operation(
            summary = "Encrypt a JSON payload",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Signature match successfully"),
                    @ApiResponse(responseCode = "400", description = "Not a valid signature")
            }
    )
    public ResponseEntity verify(@RequestBody JsonNode payload) {
        boolean isSignatureValid = this.verifySignature.handle(payload, DEFAULT_ALGORITHM);
        return isSignatureValid ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();

    }
}
