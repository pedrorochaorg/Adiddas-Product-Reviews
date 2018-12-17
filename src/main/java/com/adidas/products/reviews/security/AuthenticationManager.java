package com.adidas.products.reviews.security;

import static com.adidas.products.reviews.common.messages.Rest.INVALID_AUTHENTICATION_CREDENTIALS;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

/**
 * A custom reactive authentication manager that supports multiple authentication providers.
 *
 * @author pedrorocha
 **/
@AllArgsConstructor
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final List<AuthenticationProvider> providerList;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        if (authentication.isAuthenticated()) {
            log.debug("IsAuthenticated: {}", authentication.toString());
            return Mono.just(authentication);
        }
        Class<? extends Authentication> toTest = authentication.getClass();
        for(AuthenticationProvider provider: providerList) {
            if (provider.supports(toTest)) {
                try {
                    //The provider returns a full authenticated Authentication object.
                    //With authentication.setAuthenticated(true);
                    return Mono.just(provider.authenticate(authentication));
                } catch (Exception e) {
                    return Mono.error(new BadCredentialsException(INVALID_AUTHENTICATION_CREDENTIALS));
                }
            }
        }
        log.debug("Failed to Authenticate: {}",authentication);
        return Mono.error(new ProviderNotFoundException(INVALID_AUTHENTICATION_CREDENTIALS));

    }
}
