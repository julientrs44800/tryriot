package com.tryriot.cryptography.adapter;

import com.tryriot.cryptography.domain.port.SignatureInterface;
import com.tryriot.cryptography.exception.CreateSignatureFailureException;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import org.erdtman.jcs.JsonCanonicalizer;

@Service
public class HMACSignatureService implements SignatureInterface {

    public boolean verifySignature(JsonNode payload) {
        try {
            JsonNode signatureNode = payload.get("signature");
            JsonNode dataNode = payload.get("data");
            if (signatureNode == null || dataNode == null) {
                return false;
            }
            String expectedSignature = signPayload(dataNode);

            return expectedSignature.equals(signatureNode.asText());
        } catch (Exception e) {
            return false;
        }
    }

    public String signPayload(JsonNode payload) {
        try {
            JsonCanonicalizer canonicalizer = new JsonCanonicalizer(payload.toString());
            String canonicalJson = canonicalizer.getEncodedString();

            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec("tryriot".getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmac.init(secretKeySpec);

            byte[] signatureBytes = hmac.doFinal(canonicalJson.getBytes(StandardCharsets.UTF_8));

            return HexFormat.of().formatHex(signatureBytes);
        } catch (Exception e) {
            throw new CreateSignatureFailureException("Error signing payload" + e.getMessage());
        }
    }
}
