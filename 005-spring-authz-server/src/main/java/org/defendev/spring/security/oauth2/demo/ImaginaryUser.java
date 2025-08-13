package org.defendev.spring.security.oauth2.demo;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;



public class ImaginaryUser extends User implements CredentialsContainer {

    private final String plaintextPassword;

    private final AccessTokenClaims accessTokenClaims;

    public ImaginaryUser(String username, String plaintextPassword, String encodedPassword,
                         Collection<? extends GrantedAuthority> authorities, AccessTokenClaims accessTokenClaims) {
        super(username, encodedPassword, authorities);
        this.plaintextPassword = plaintextPassword;
        this.accessTokenClaims = accessTokenClaims;
    }

    public String getPlaintextPassword() {
        return plaintextPassword;
    }

    public AccessTokenClaims getAccessTokenClaims() {
        return accessTokenClaims;
    }

    public ImaginaryUser copy() {
        final List<GrantedAuthority> authoritiesCopy = new ArrayList<>();
        authoritiesCopy.addAll(getAuthorities());
        return new ImaginaryUser(getUsername(), plaintextPassword, getPassword(), authoritiesCopy, accessTokenClaims);
    }

    public record AccessTokenClaims(
        String oid,
        String upn,
        String givenName,
        String familyName,
        List<String> groups
    ) { }

    public static class AccessTokenClaimsBuilder {

        private String oid;

        private String upn;

        private String givenName;

        private String familyName;

        private List<String> groups;

        public AccessTokenClaimsBuilder setOid(String oid) {
            this.oid = oid;
            return this;
        }

        public AccessTokenClaimsBuilder setUpn(String upn) {
            this.upn = upn;
            return this;
        }

        public AccessTokenClaimsBuilder setGivenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public AccessTokenClaimsBuilder setFamilyName(String familyName) {
            this.familyName = familyName;
            return this;
        }

        public AccessTokenClaimsBuilder setGroups(List<String> groups) {
            this.groups = groups;
            return this;
        }

        public AccessTokenClaims build() {
            return new AccessTokenClaims(oid, upn, givenName, familyName, groups);
        }
    }

    public static class ImaginaryUserBuilder {

		private String username;

        private String plaintextPassword;

        private List<GrantedAuthority> authorities = new ArrayList<>();

        private Function<String, String> passwordEncoder;

        private AccessTokenClaims accessTokenClaims;

        public ImaginaryUserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public ImaginaryUserBuilder setPlaintextPassword(String plaintextPassword) {
            this.plaintextPassword = plaintextPassword;
            return this;
        }

        public ImaginaryUserBuilder setAuthorities(List<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public ImaginaryUserBuilder setPasswordEncoder(Function<String, String> passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            return this;
        }

        public ImaginaryUserBuilder setAccessTokenClaims(AccessTokenClaims accessTokenClaims) {
            this.accessTokenClaims = accessTokenClaims;
            return this;
        }

        public ImaginaryUser buildAzureCompatible() {
            assertThat(accessTokenClaims).as("Azure requires accessTokenClaims").isNotNull();
            assertThat(accessTokenClaims.oid).as("Azure requires accessTokenClaims.oid").isNotNull();
            assertThat(accessTokenClaims.upn).as("Azure requires accessTokenClaims.upn").isNotNull();
            assertThat(accessTokenClaims.givenName).as("Azure requires accessTokenClaims.givenName").isNotNull();
            assertThat(accessTokenClaims.familyName).as("Azure requires accessTokenClaims.familyName").isNotNull();
            assertThat(accessTokenClaims.groups).as("Azure requires accessTokenClaims.groups").isNotNull();

			final String encodedPassword = this.passwordEncoder.apply(this.plaintextPassword);
            return new ImaginaryUser(this.username, plaintextPassword, encodedPassword, this.authorities,
                this.accessTokenClaims);
        }
    }

}
