package com.tryriot.cryptography.service;

import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.domain.port.SignatureInterface;
import com.tryriot.cryptography.adapter.HMACSignatureService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SignatureResolverService {
    private HMACSignatureService hmacSignatureService;

    public SignatureInterface resolve(CryptographyAlgorithm algorithm) {
        switch (algorithm) {
            case HMAC:
                return this.hmacSignatureService;
            default:
                throw new RuntimeException("Impossible case.");
        }
    }
}
