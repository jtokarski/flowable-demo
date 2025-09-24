package org.defendev.spring.security.oauth2.demo;

import org.defendev.spring.security.oauth2.demo.dto.KeyDto;
import org.defendev.spring.security.oauth2.demo.dto.KeyResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Base64;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.web.bind.annotation.RequestMethod.GET;



@RequestMapping(path = {"/top-secret"})
@Controller
public class PrivateKeyDisclosureController {

    private final DefendevGeneratedKeys generatedKeys;

    @Autowired
    public PrivateKeyDisclosureController(DefendevGeneratedKeys generatedKeys) {
        this.generatedKeys = generatedKeys;
    }

    /*
     * Yes! I know that exposing private keys like this would be absurd in production system.
     * But keep in mind that this Authorization Server is mainly for mocking purposes.
     *
     * In fact this private key is exposed so that I can hijack it, and generate valid
     * Access Tokens for testing Resource Server with RestAssured.
     *
     */
    @RequestMapping(method = GET, path = "private-key")
    public ResponseEntity<Resource> disclosePrivateKey() {
        final ContentDisposition contentDisposition = ContentDisposition.attachment()
            .filename("private_pkcs8.key")
            .build();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        final byte[] keyBytes = generatedKeys.getKeyData().getFirst().keyPair().getPrivate().getEncoded();
        final ByteArrayResource fileResource = new ByteArrayResource(keyBytes);
        return ResponseEntity.ok()
            .contentType(APPLICATION_OCTET_STREAM)
            .contentLength(fileResource.contentLength())
            .headers(headers)
            .body(fileResource);
    }

    @RequestMapping(method = GET, path = "private-key-all")
    public ResponseEntity<KeyResponseDto> disclosePrivateKeys() {
        final List<KeyDto> dtos = generatedKeys.getKeyData().stream().map(generatedKeyPair -> {
            final byte[] keyBytes = generatedKeyPair.keyPair().getPrivate().getEncoded();
            final String privateKeyEncoded = Base64.getEncoder().encodeToString(keyBytes);
            return new KeyDto(generatedKeyPair.keyId(), privateKeyEncoded);
        }).toList();
        return ResponseEntity.ok().contentType(APPLICATION_JSON).body(new KeyResponseDto(dtos));
    }

}
