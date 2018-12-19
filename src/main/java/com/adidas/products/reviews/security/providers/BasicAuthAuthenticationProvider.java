package com.adidas.products.reviews.security.providers;

import static com.adidas.products.reviews.common.messages.Rest.INVALID_API_KEY;
import static com.adidas.products.reviews.common.messages.Rest.INVALID_BASIC_AUTH;

import com.adidas.products.reviews.config.BasicAuthConfig;
import com.adidas.products.reviews.config.models.BasicAuth;
import com.adidas.products.reviews.security.models.BasicAuthAuthentication;
import com.adidas.products.reviews.security.models.Identity;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Basic Auth authentication manager, performs authentication based on an request containing a BasicAuthAuthentication
 * object.
 *
 * @author pedrorocha
 **/
@AllArgsConstructor
@Slf4j
public class BasicAuthAuthenticationProvider implements AuthenticationProvider {

    private final BasicAuthConfig config;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String basicAuth = (String) authentication.getPrincipal();
        log.debug("BasicAuthProvider: {}", basicAuth);

        if (basicAuth == null || basicAuth.isEmpty()) {
            throw new BadCredentialsException(INVALID_BASIC_AUTH);
        }

        String[] credentials = extractAndDecodeHeader(basicAuth);

        log.debug("BasicAuthProvider: {}", credentials[0]);
        log.debug("BasicAuthProvider: {}", credentials[1]);

        BasicAuth basicAuthObject = config.getByUsername(credentials[0]);

        if (basicAuthObject == null || !basicAuthObject.getPassword().equals(credentials[1])) {
            throw new BadCredentialsException(INVALID_BASIC_AUTH);
        }
        log.debug("BasicAuthProvider: {}", basicAuthObject.toString());


        String authorities = String.join(",", basicAuthObject.getRoles());
        log.debug("BasicAuthProvider: {}", authorities);
        return new BasicAuthAuthentication(
                new Identity(
                        basicAuthObject.getUsername(),
                        basicAuthObject.getPassword(),
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                ),
                basicAuthObject.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        );

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BasicAuthAuthentication.class);
    }

    private String[] extractAndDecodeHeader(String base64String) throws BadCredentialsException {
        byte[] base64Token = base64String.getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        String token = "";
        try {
            decoded = Base64.getDecoder().decode(base64Token);
            token = new String(decoded, StandardCharsets.UTF_8);

        } catch (IllegalArgumentException ex) {
            throw new BadCredentialsException(
                    INVALID_BASIC_AUTH
            );
        }

        int delimiter = token.indexOf(":");

        if (delimiter == -1) {
            throw new BadCredentialsException(
                    INVALID_BASIC_AUTH
            );
        }

        return new String[]{token.substring(0, delimiter), token.substring(delimiter + 1)};
    }
}
