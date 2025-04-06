package com.tryriot.cryptography.service;

import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.adapter.HMACSignatureService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignatureResolverServiceTest {

    // Not very useful with one algorithm, but let's keep it for future extensibility
    @ParameterizedTest
    @MethodSource("algorithmProvider")
    void testResolve(CryptographyAlgorithm algorithm, boolean shouldThrow) {
        HMACSignatureService mockService = mock(HMACSignatureService.class);
        SignatureResolverService resolver = new SignatureResolverService(mockService);

        if (shouldThrow) {
            assertThrows(RuntimeException.class, () -> resolver.resolve(algorithm));
        } else {
            assertEquals(mockService, resolver.resolve(algorithm));
        }
    }

    private static Stream<Arguments> algorithmProvider() {
        return Stream.of(
            Arguments.of(CryptographyAlgorithm.HMAC, false),
            Arguments.of(CryptographyAlgorithm.BASE64, true)
        );
    }
}
