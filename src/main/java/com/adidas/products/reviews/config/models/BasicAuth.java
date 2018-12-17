package com.adidas.products.reviews.config.models;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Modular object to store BasicAuth values used for configurations purposes.
 *
 * @author pedrorocha
 **/
@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class BasicAuth implements Serializable {

    private String username;
    private String password;
    private List<String> roles;

    /**
     * Simplified constructor.
     */
    public BasicAuth() {
    }


}
