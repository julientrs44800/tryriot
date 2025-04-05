package com.tryriot.cryptography.domain.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.domain.port.EncodingInterface;
import com.tryriot.cryptography.service.EncodingResolverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DecryptPayload {
    private EncodingResolverService encodingResolverService;

    public JsonNode handle(JsonNode inputToDecrypt, CryptographyAlgorithm algorithm) {
        EncodingInterface encryptStrategyService = this.encodingResolverService.resolve(algorithm);

        return encryptStrategyService.decryptPayload(inputToDecrypt);
    }
}
