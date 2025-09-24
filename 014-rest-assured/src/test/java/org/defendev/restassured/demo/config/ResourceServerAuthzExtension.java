package org.defendev.restassured.demo.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import static io.restassured.RestAssured.get;
import static java.util.Objects.nonNull;
import static org.assertj.core.api.Assertions.assertThat;


/*
    Note on algorithms to use:

    azureIss:
     {
         "typ": "JWT",
         "alg": "RS256",
         "x5t": "HS23b7Do7TcaU1RoLHwpIq24VYg",
         "kid": "HS23b7Do7TcaU1RoLHwpIq24VYg"
     }

    sprin6authzIss:
    {
      "kid": "875edec5-aa30-402a-aadd-aa37bb014d50",
      "alg": "RS256"
    }

*/
public class ResourceServerAuthzExtension implements ParameterResolver {

    private static final ExtensionContext.Namespace NAMESPACE =
        ExtensionContext.Namespace.create(ResourceServerAuthzExtension.class);

    private static final TypeRef<MockAuthzKeyResponseDto> mockAuthzKeyResponseRef = new TypeRef<>() { };

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException
    {
        final AccessToken accessTokenAnnotation = parameterContext.getParameter().getAnnotation(AccessToken.class);
        final boolean isString = String.class.equals(parameterContext.getParameter().getType());
        return isString && nonNull(accessTokenAnnotation);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException
    {
        final ExtensionContext rootContext = extensionContext.getRoot();
        final ExtensionContext.Store store = rootContext.getStore(NAMESPACE);
        return store.getOrComputeIfAbsent("filter", this::generateJwt);
    }

    private String generateJwt(String key) {

        final MockAuthzKeyResponseDto keysDto = get("http://localhost:8010/defendev-authz/top-secret/private-key-all")
            .as(mockAuthzKeyResponseRef);

        final MockAuthzKeyDto firstKey = keysDto.keys().getFirst();
        final byte[] privateKeyBytes = Base64.getDecoder().decode(firstKey.privateKey());

        try {
            final PKCS8EncodedKeySpec privateSpecLoaded = new PKCS8EncodedKeySpec(privateKeyBytes);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final RSAPrivateKey privateKeyLoaded = (RSAPrivateKey) keyFactory.generatePrivate(privateSpecLoaded);
            assertThat(privateKeyLoaded).isNotNull();

            final Algorithm algorithm = Algorithm.RSA256(null, privateKeyLoaded);

            final String token = JWT.create()
                .withSubject("user123")
                .withExpiresAt(Instant.now().plus(4, ChronoUnit.HOURS))
                .withKeyId(firstKey.keyId())
                .withIssuer("http://localhost:8010/defendev-authz")
                /*
                 * todo: other claims
                 *
                 */
                .sign(algorithm);

            return token;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

}
