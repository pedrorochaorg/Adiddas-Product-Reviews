package com.adidas.products.reviews.config;

import com.adidas.products.reviews.config.models.ApiKey;
import com.adidas.products.reviews.config.models.BasicAuth;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Set of configurable properties that store the basicAuth know by the application.
 *
 * @author pedrorocha
 **/
@Data
@Slf4j
@Configuration
@PropertySource("classpath:application.yaml")
@ConfigurationProperties(prefix = "spring.security")
public class BasicAuthConfig {

    private List<BasicAuth> basicAuth;

    /**
     * Log's the current property values in the output log.
     */
    @PostConstruct
    public void log() {
        log.debug("BasicAuth: {}", basicAuth.toString());
    }

    /**
     * Returns an BasicAuth object which username property value matches the value of the arg username.
     *
     * @param username username to search for
     * @return null or an basicAuth object.
     */
    public BasicAuth getByUsername(String username) {
        Optional<BasicAuth> basicAuthResult = basicAuth.stream()
                .filter(item -> item.getUsername().equals(username)).findFirst();

        if (!basicAuthResult.isPresent()) {
            return null;
        }

        return basicAuthResult.get();
    }

}
