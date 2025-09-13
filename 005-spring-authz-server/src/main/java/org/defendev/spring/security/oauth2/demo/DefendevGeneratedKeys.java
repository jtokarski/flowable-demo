package org.defendev.spring.security.oauth2.demo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;
import java.util.stream.IntStream;
import java.security.NoSuchAlgorithmException;


public class DefendevGeneratedKeys {

    private final List<KeyPair> keyPairs;

    public DefendevGeneratedKeys(int keyPairCount) {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPairs = IntStream.range(0, keyPairCount)
                .mapToObj(i -> keyPairGenerator.generateKeyPair())
                .toList();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<KeyPair> keyPairs() {
        return keyPairs;
    }
}
