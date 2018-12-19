package com.adidas.products.reviews.security.models;

import java.util.Collection;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * Basic Auth Authentication Holder. Used to delegate authentication methods to the right authentication provider.
 *
 * @author pedrorocha
 **/
@ToString(callSuper = true)
public class BasicAuthAuthentication extends PreAuthenticatedAuthenticationToken {

    /**
     * Instantiates a new BasicAuthAuthentication object.
     *
     * @param aPrincipal authentication principal.
     */
    public BasicAuthAuthentication(Object aPrincipal) {
        super(aPrincipal, null);
        this.setAuthenticated(false);
    }
    /**
     * Instantiates a new BasicAuthAuthentication object.
     *
     * @param aPrincipal authentication principal.
     * @param aCredentials credentials.
     */
    public BasicAuthAuthentication(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
        this.setAuthenticated(false);
    }

    /**
     * Instantiates a new BasicAuthAuthentication object.
     *
     * @param aPrincipal authentication principal.
     * @param aCredentials credentials.
     * @param anAuthorities set of granted authorities.
     */
    public BasicAuthAuthentication(
            Object aPrincipal,
            Object aCredentials,
            Collection<? extends GrantedAuthority> anAuthorities
    ) {
        super(aPrincipal, aCredentials, anAuthorities);
        this.setAuthenticated(true);
    }
}
