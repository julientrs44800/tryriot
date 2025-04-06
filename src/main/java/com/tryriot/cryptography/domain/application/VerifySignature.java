package com.tryriot.cryptography.domain.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.domain.port.SignatureInterface;
import com.tryriot.cryptography.service.SignatureResolverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class VerifySignature {
    private SignatureResolverService signatureResolverService;

    public boolean handle(JsonNode payload, CryptographyAlgorithm algorithm) {
        SignatureInterface signatureStrategyService = this.signatureResolverService.resolve(algorithm);

        return signatureStrategyService.verifySignature(payload);
    }
}
