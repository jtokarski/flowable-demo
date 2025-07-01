package org.defendev.spring.security.oauth2.demo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;



/*
 * It's better than o.s.s.p.InMemoryUserDetailsManager because it
 *   - on loadUserByUsername() doesn't create new o.s.s.c.u.User but
 *   - preserves the original user object class (ImaginaryUser)
 *
 * By the way I learnt that o.s.s.c.u.User implements o.s.s.c.CredentialsContainer
 *
 */
public class BetterInMemoryUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    private final List<ImaginaryUser> users;

    public BetterInMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        users = new ArrayList<>();

        users.add(new ImaginaryUser.ImaginaryUserBuilder()
            .setUsername("isaac_newton")
            .setPassword("password")
            .setPasswordEncoder(passwordEncoder::encode)
            .setAccessTokenClaims(new ImaginaryUser.AccessTokenClaimsBuilder()
                .setOid("9eef4ff3-19cb-4ebd-821d-80019f6b42e1")
                .setUpn("isaac.newton@mock.org")
                .setGivenName("Isaac")
                .setFamilyName("Newton")
                .setGroups(List.of())
                .build()
            )
            .buildAzureCompatible()
        );
        users.add(new ImaginaryUser.ImaginaryUserBuilder()
            .setUsername("Alberto_Ein5")
            .setPassword("password2")
            .setPasswordEncoder(passwordEncoder::encode)
            .setAccessTokenClaims(new ImaginaryUser.AccessTokenClaimsBuilder()
                .setOid("e779211d-3e93-4ddf-86d1-f513075b0e4e")
                .setUpn("albert.einstein@example.edu")
                .setGivenName("Albert")
                .setFamilyName("Einstein")
                .setGroups(List.of())
                .build()
            )
            .buildAzureCompatible()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.stream()
            .filter(u -> u.getUsername().equals(username))
            /*
             * Copy must be created because password is being o.s.s.c.CredentialsContainer.eraseCredentials()
             *
             */
            .map(u -> u.copy())
            .findAny()
            .orElseThrow(
                () -> new UsernameNotFoundException("user '" + username + "' not found")
            );
    }

}
