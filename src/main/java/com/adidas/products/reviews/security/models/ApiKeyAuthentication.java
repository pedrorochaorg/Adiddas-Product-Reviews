package com.adidas.products.reviews.security.models;

import java.util.Collection;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Api Key Authentication Holder. Used to delegate authentication methods to the right authentication provider.
 *
 * @author pedrorocha
 **/
@ToString(callSuper = true)
public class ApiKeyAuthentication extends PreAuthenticatedAuthenticationToken {

    /**
     * Instantiates a new ApiKeyAuthentication object.
     *
     * @param aPrincipal authentication principal.
     */
    public ApiKeyAuthentication(Object aPrincipal) {
        super(aPrincipal, null);
        this.setAuthenticated(false);
    }

    /**
     * Instantiates a new ApiKeyAuthentication object.
     *
     * @param aPrincipal authentication principal.
     * @param aCredentials credentials.
     */
    public ApiKeyAuthentication(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
        this.setAuthenticated(false);
    }

    /**
     * Instantiates a new ApiKeyAuthentication object.
     *
     * @param aPrincipal authentication principal.
     * @param aCredentials credentials.
     * @param anAuthorities set of granted authorities.
     */
    public ApiKeyAuthentication(
            Object aPrincipal,
            Object aCredentials,
            Collection<? extends GrantedAuthority> anAuthorities
    ) {
        super(aPrincipal, aCredentials, anAuthorities);
        this.setAuthenticated(true);
    }
}
