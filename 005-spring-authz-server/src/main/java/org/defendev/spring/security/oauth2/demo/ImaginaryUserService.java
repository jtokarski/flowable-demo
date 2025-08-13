package org.defendev.spring.security.oauth2.demo;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;



/*
 * There were two reasons for extracting this service from BetterInMemoryUserDetailsService:
 *   - being able in implement endpoint mock for Microsoft Graph v1.0 user endpoint
 *     (https://graph.microsoft.com/v1.0/users)
 *   - iterate in Thymeleaf to generate quick-login button for each user
 *
 */
public class ImaginaryUserService {

    private final PasswordEncoder passwordEncoder;

    private final List<ImaginaryUser> users;

    public ImaginaryUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        users = new ArrayList<>();

        users.add(new ImaginaryUser.ImaginaryUserBuilder()
            .setUsername("isaac_newton")
            .setPlaintextPassword("password")
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
            .setPlaintextPassword("password2")
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

    public List<ImaginaryUser> getUsers() {
        return users;
    }

}
