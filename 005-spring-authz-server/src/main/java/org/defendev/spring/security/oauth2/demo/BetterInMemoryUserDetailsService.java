package org.defendev.spring.security.oauth2.demo;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



/*
 * It's better than o.s.s.p.InMemoryUserDetailsManager because it
 *   - on loadUserByUsername() doesn't create new o.s.s.c.u.User but
 *   - preserves the original user object class (ImaginaryUser)
 *
 * By the way I learnt that o.s.s.c.u.User implements o.s.s.c.CredentialsContainer
 *
 */
public class BetterInMemoryUserDetailsService implements UserDetailsService {

    private final ImaginaryUserService imaginaryUserService;

    public BetterInMemoryUserDetailsService(ImaginaryUserService imaginaryUserService) {
        this.imaginaryUserService = imaginaryUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return imaginaryUserService.getUsers().stream()
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
