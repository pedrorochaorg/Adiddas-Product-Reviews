package com.adidas.products.reviews.security.models;

import com.adidas.products.reviews.security.enums.IdentityType;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author pedrorocha
 **/
@Data
@AllArgsConstructor
public class Identity implements UserDetails {

    public String key;
    public Collection<? extends GrantedAuthority> authorities;
    public String username;
    public String password;
    public IdentityType type;

    /**
     * Instantiates a new Identity Object settings the type to API_KEY.
     *
     * @param key Api Key
     * @param authorities set of authorities.
     * @param username Api Key Subject
     */
    public Identity(
            String key,
            Collection<? extends GrantedAuthority> authorities,
            String username
    ) {
        this.key = key;
        this.authorities = authorities;
        this.username = username;
        this.type = IdentityType.API_KEY;
    }

    /**
     * Instantiates a new Identity Object settings the type to BASIC_AUTH.
     *
     * @param username username
     * @param password password
     * @param authorities set of authorities.
     */
    public Identity(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.authorities = authorities;
        this.username = username;
        this.password = password;
        this.type = IdentityType.BASIC_AUTH;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
