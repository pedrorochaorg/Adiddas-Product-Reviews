package com.adidas.products.reviews.config.models;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * Modular object to store ApiKey values used for configurations purposes.
 *
 * @author pedrorocha
 **/
@Data
@ToString(callSuper = true)
@AllArgsConstructor
public class ApiKey implements Serializable {

    private String name;
    private String key;
    private List<String> roles;

    /**
     * Simplified constructor.
     */
    public ApiKey() {
    }


}
