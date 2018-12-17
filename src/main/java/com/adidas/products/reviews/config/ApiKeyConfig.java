package com.adidas.products.reviews.config;

import com.adidas.products.reviews.config.models.ApiKey;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.swing.text.html.Option;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Set of configurable properties that store the apiKeys know by the application.
 *
 * @author pedrorocha
 **/
@Data
@Slf4j
@Configuration
@PropertySource("classpath:application.yaml")
@ConfigurationProperties(prefix = "spring.security")
public class ApiKeyConfig {

    private List<ApiKey> apiKeys;

    /**
     * Log's the current property values in the output log.
     */
    @PostConstruct
    public void log() {
        log.debug("ApiKeys: {}", apiKeys.toString());
    }

    /**
     * Returns an apiKey object which key property value matches the value of the arg key.
     *
     * @param key api key to search for
     * @return null or an apiKey object.
     */
    public ApiKey getByKey(String key) {
        Optional<ApiKey> apiKey = apiKeys.stream().filter(item -> item.getKey().equals(key)).findFirst();

        if (!apiKey.isPresent()) {
            return null;
        }

        return apiKey.get();
    }

}
