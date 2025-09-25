package org.defendev.spring.security.oauth2.client.demo;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.defendev.spring.security.oauth2.client.demo.dto.MockAuthzKeyDto;
import org.defendev.spring.security.oauth2.client.demo.dto.MockAuthzKeyResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



public class NimbusSignTokenTest {

    private static final ParameterizedTypeReference<MockAuthzKeyResponseDto> mockAuthzKeyResponseRef =
        new ParameterizedTypeReference<>() { };

    private final RestClient restClient;

    public NimbusSignTokenTest() {
        restClient = RestClient.builder()
            .build();
    }

    private PrivateKey decodePrivateKey(MockAuthzKeyDto dto) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final byte[] privateKeyBytes = Base64.getDecoder().decode(dto.privateKey());
        final PKCS8EncodedKeySpec privateSpecLoaded = new PKCS8EncodedKeySpec(privateKeyBytes);
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final RSAPrivateKey privateKeyLoaded = (RSAPrivateKey) keyFactory.generatePrivate(privateSpecLoaded);
        return privateKeyLoaded;
    }

    private MockAuthzKeyDto fetchPrivateKeys() {
        try {
            final MockAuthzKeyResponseDto keysDto = restClient.get()
                .uri(new URI("http://localhost:8010/defendev-authz/top-secret/private-key-all"))
                .retrieve()
                .body(mockAuthzKeyResponseRef);
            return keysDto.keys().getFirst();
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }

    /*
     * Relies on running:
     *   - 005-spring-authz-server
     *   - 010-spring-resource-server
     *
     */
    @Test
    public void shouldCreateAndSignToken() throws JOSEException, URISyntaxException, NoSuchAlgorithmException,
        InvalidKeySpecException
    {
        final MockAuthzKeyDto mockAuthzKeyDto = fetchPrivateKeys();
        final PrivateKey privateKeyLoaded = decodePrivateKey(mockAuthzKeyDto);
        final RSASSASigner signer = new RSASSASigner(privateKeyLoaded);

        final JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.RS256)
            .keyID(mockAuthzKeyDto.keyId())
            .build();

        final Instant issueTime = Instant.now();
        final Instant expirationTime = issueTime.plus(30, ChronoUnit.MINUTES);
        final JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject("nikolaus_otto")
            .issuer("http://localhost:8010/defendev-authz")
            .issueTime(Date.from(issueTime))
            .expirationTime(Date.from(expirationTime))
            .jwtID(UUID.randomUUID().toString())
            .claim("oid", "e779211d-3e93-4ddf-86d1-f513075b0e4e")
            .claim("given_name", "Nikolaus")
            .claim("family_name", "Otto")
            .build();

        final SignedJWT signedJWT = new SignedJWT(jwsHeader, claimsSet);
        signedJWT.sign(signer);
        final String accessToken = signedJWT.serialize();

        final ResponseEntity<String> resourceServerResponse = restClient.get()
            .uri(new URI("http://localhost:8012/backweb/hello"))
            .headers(headers -> headers.setBearerAuth(accessToken))
            .retrieve()
            .toEntity(String.class);

        assertThat(resourceServerResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

}
