package org.defendev.restassured.demo.junit.extension;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Parameter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static java.util.Objects.isNull;
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
        throws ParameterResolutionException {
        final Parameter parameter = parameterContext.getParameter();
        return isAccessTokenParameter(parameter) || isAccessTokenCacheParameter(parameter);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException
    {
        final Parameter parameter = parameterContext.getParameter();
        final ExtensionContext rootContext = extensionContext.getRoot();
        final ExtensionContext.Store store = rootContext.getStore(NAMESPACE);
        final LoadingCache<Actor, String> accessTokenCache = (LoadingCache<Actor, String>) store.getOrComputeIfAbsent(
            "access_token_cache", ResourceServerAuthzExtension::buildAccessTokenCache);

        if (isAccessTokenParameter(parameter)) {
            final AccessToken accessTokenAnnotation = parameterContext.getParameter().getAnnotation(AccessToken.class);
            final Actor actor = accessTokenAnnotation.value();
            if (isNull(actor)) {
                throw new IllegalArgumentException("The actor have to be specified for @AccessToken annotation");
            }
            return accessTokenCache.get(actor);
        } else if (isAccessTokenCacheParameter(parameter)) {
            return accessTokenCache;
        } else {
            throw new IllegalArgumentException("Can not resolve properly parameter");
        }
    }

    private boolean isAccessTokenParameter(Parameter parameter) {
        final AccessToken accessTokenAnnotation = parameter.getAnnotation(AccessToken.class);
        final AccessTokenCache accessTokenCacheAnnotation = parameter.getAnnotation(AccessTokenCache.class);
        final boolean isString = String.class.equals(parameter.getType());
        return isString && nonNull(accessTokenAnnotation) && isNull(accessTokenCacheAnnotation);
    }

    private boolean isAccessTokenCacheParameter(Parameter parameter) {
        final AccessToken accessTokenAnnotation = parameter.getAnnotation(AccessToken.class);
        final AccessTokenCache accessTokenCacheAnnotation = parameter.getAnnotation(AccessTokenCache.class);
        final boolean isLoadingCache = LoadingCache.class.equals(parameter.getType());
        return isLoadingCache && nonNull(accessTokenCacheAnnotation) && isNull(accessTokenAnnotation);
    }

    private static LoadingCache<Actor, String> buildAccessTokenCache(String storeKey) {
        return Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(59, TimeUnit.MINUTES)
            .build(ResourceServerAuthzExtension::generateSignedJwt);
    }

    private static String generateSignedJwt(Actor actor) {
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
                .withSubject(actor.getSubject())
                .withExpiresAt(Instant.now().plus(4, ChronoUnit.HOURS))
                .withKeyId(firstKey.keyId())
                .withIssuer("http://localhost:8010/defendev-authz")
                .withClaim("oid", actor.getOid())
                .withClaim("family_name", actor.getFamilyName())
                .withClaim("given_name", actor.getGivenName())
                .sign(algorithm);

            return token;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        }
    }

}
