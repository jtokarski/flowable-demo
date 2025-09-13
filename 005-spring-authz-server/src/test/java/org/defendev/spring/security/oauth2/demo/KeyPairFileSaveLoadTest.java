package org.defendev.spring.security.oauth2.demo;

import org.apache.commons.lang3.Validate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;



public class KeyPairFileSaveLoadTest {

    private static final String OUTPUT_FILES_LOCATION = "C:/dev/flowable-demo/005-spring-authz-server/";

    /*
     * This test is POC of saving KeyPair to a text file and loading it from text file.
     * I've written this to prepare for creating and endpoint in Mock Authorization Server
     * that would reveal the secret key, so that I can hijack it, generate and sign JWT
     * and make valid requests to a Resource Server.
     * The JWT generation and signing is different place - see 014-rest-assured
     *
     */
    @Test
    public void shouldGenrateSaveAndLoadKeyPair() throws NoSuchAlgorithmException, IOException,
        InvalidKeySpecException {
        // generate Key Pair
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // get private and public key
        final PrivateKey privateKey = keyPair.getPrivate();
        final PublicKey publicKey = keyPair.getPublic();

        final PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        final Path privateKeyOutputPath = outputFilePath("private_pkcs8.key");
        Files.write(privateKeyOutputPath, privateSpec.getEncoded());

        final X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKey.getEncoded());
        final Path publicKeyOutputPath = outputFilePath("public_x509.key");
        Files.write(publicKeyOutputPath, publicSpec.getEncoded());


        final byte[] privateKeyBytes = Files.readAllBytes(privateKeyOutputPath);
        final byte[] publicKeyBytes = Files.readAllBytes(publicKeyOutputPath);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        final PKCS8EncodedKeySpec privateSpecLoaded = new PKCS8EncodedKeySpec(privateKeyBytes);
        final PrivateKey privateKeyLoaded = keyFactory.generatePrivate(privateSpecLoaded);

        final X509EncodedKeySpec publicSpecLoaded = new X509EncodedKeySpec(publicKeyBytes);
        final PublicKey publicKeyLoaded = keyFactory.generatePublic(publicSpecLoaded);

        assertThat(privateKeyLoaded.getAlgorithm()).isEqualTo("RSA");
        assertThat(publicKeyLoaded.getAlgorithm()).isEqualTo("RSA");
        assertThat(privateKey).isEqualTo(privateKeyLoaded);
        assertThat(publicKey).isEqualTo(publicKeyLoaded);
    }

    private Path outputFilePath(String filename) {
        Validate.notBlank(filename);
        final Path path = Path.of(OUTPUT_FILES_LOCATION, filename);
        if (Files.notExists(path.getParent())) {
            throw new IllegalArgumentException("Output files location does not exist");
        }
        return path;
    }

}
