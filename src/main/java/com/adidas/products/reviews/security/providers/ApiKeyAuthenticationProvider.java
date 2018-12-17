package com.adidas.products.reviews.security.providers;

import static com.adidas.products.reviews.common.messages.Rest.INVALID_API_KEY;

import com.adidas.products.reviews.config.ApiKeyConfig;
import com.adidas.products.reviews.config.models.ApiKey;
import com.adidas.products.reviews.security.models.ApiKeyAuthentication;
import com.adidas.products.reviews.security.models.Identity;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Api Key authentication provider, performs authentication based on an request containing a ApiKeyAuthentication
 * object.
 *
 * @author pedrorocha
 **/
@AllArgsConstructor
@Slf4j
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    private final ApiKeyConfig config;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("ApiKeyProvider: {}", authentication.getPrincipal());
        String apiKey = (String) authentication.getPrincipal();

        if (apiKey == null || apiKey.isEmpty()) {
            throw new BadCredentialsException(INVALID_API_KEY);
        }

        ApiKey apiKeyObject = config.getByKey(apiKey);
        if (apiKeyObject == null) {
            throw new BadCredentialsException(INVALID_API_KEY);
        }

        String authorities = apiKeyObject.getRoles().stream().collect(Collectors.joining(","));
        return new ApiKeyAuthentication(
                new Identity(
                        apiKeyObject.getKey(),
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities),
                        apiKeyObject.getName()
                ),
                apiKeyObject.getKey(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        );

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(ApiKeyAuthentication.class);
    }
}
