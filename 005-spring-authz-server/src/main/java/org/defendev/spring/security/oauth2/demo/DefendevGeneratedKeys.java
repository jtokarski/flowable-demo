package org.defendev.spring.security.oauth2.demo;

import org.apache.commons.lang3.RandomStringUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.IntStream;



public class DefendevGeneratedKeys {

    private final List<KeyPairWithId> keyData;

    public record KeyPairWithId(String keyId, KeyPair keyPair) { }

    public DefendevGeneratedKeys(int keyPairCount) {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyData = IntStream.range(0, keyPairCount)
                .mapToObj(i -> {
                    final String keyId = RandomStringUtils.secure().nextAlphabetic(8);
                    final KeyPair keyPair = keyPairGenerator.generateKeyPair();
                    return new KeyPairWithId(keyId, keyPair);
                })
                .toList();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<KeyPairWithId> getKeyData() {
        return keyData;
    }
}
