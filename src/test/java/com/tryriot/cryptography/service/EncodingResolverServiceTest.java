package com.tryriot.cryptography.service;

import com.tryriot.common.constants.CryptographyAlgorithm;
import com.tryriot.cryptography.adapter.Base64EncodingService;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class EncodingResolverServiceTest {

    // Not very useful with one algorithm, but let's keep it for future extensibility
    @ParameterizedTest
    @MethodSource("algorithmProvider")
    public void testResolveAlgorithm(CryptographyAlgorithm algorithm, boolean shouldThrow) {
        Base64EncodingService mockService = mock(Base64EncodingService.class);
        EncodingResolverService resolver = new EncodingResolverService(mockService);

        if (shouldThrow) {
            RuntimeException exception = assertThrows(RuntimeException.class, () -> resolver.resolve(algorithm));
            assertEquals("Impossible case.", exception.getMessage());
        } else {
            assertEquals(mockService, resolver.resolve(algorithm));
        }
    }

    private static Stream<Arguments> algorithmProvider() {
        return Stream.of(
            Arguments.of(CryptographyAlgorithm.BASE64, false),
            Arguments.of(CryptographyAlgorithm.HMAC, true)
        );
    }
}
