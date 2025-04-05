package com.tryriot.cryptography.service;

import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.domain.port.EncodingInterface;
import com.tryriot.cryptography.adapter.Base64EncodingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EncodingResolverService {
    private Base64EncodingService base64EncodingService;

    public EncodingInterface resolve(CryptographyAlgorithm algorithm) {
        switch (algorithm) {
            case BASE64:
                return this.base64EncodingService;
            default:
                throw new RuntimeException("Impossible case.");
        }
    }
}
