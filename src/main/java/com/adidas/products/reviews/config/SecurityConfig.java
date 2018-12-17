package com.adidas.products.reviews.config;

import com.adidas.products.reviews.security.AuthenticationEntryPoint;
import com.adidas.products.reviews.security.AuthenticationManager;
import com.adidas.products.reviews.security.filters.ApiKeyAuthenticationFilter;
import com.adidas.products.reviews.security.filters.BasicAuthAuthenticationFilter;
import com.adidas.products.reviews.security.providers.ApiKeyAuthenticationProvider;
import com.adidas.products.reviews.security.providers.BasicAuthAuthenticationProvider;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

/**
 * Configures WebFlux security.
 *
 * @author pedrorocha
 **/
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(proxyTargetClass = true)
public class SecurityConfig {

    private final ServerAuthenticationEntryPoint entryPoint = new AuthenticationEntryPoint();
    private final ServerSecurityContextRepository repository = new WebSessionServerSecurityContextRepository();


    @Bean
    SecurityWebFilterChain springWebFilterChain(
            ServerHttpSecurity http,
            ReactiveAuthenticationManager manager,
            @Qualifier("basicAuthProvider") AuthenticationProvider basicAuth,
            @Qualifier("apiKeyAuthProvider") AuthenticationProvider apiKeyAuth
    ) throws Exception {
        return http
                .authorizeExchange()
                .anyExchange().permitAll()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .logout().disable()
                .cors().and()
                //Override BasicAuthenticationEntryPoint
                .exceptionHandling().authenticationEntryPoint(this.entryPoint)
                .and()
                .authenticationManager(manager)
                .addFilterAt(new ApiKeyAuthenticationFilter(manager,repository,entryPoint),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(new BasicAuthAuthenticationFilter(manager,repository,entryPoint),
                        SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean(name = "apiKeyAuthProvider")
    AuthenticationProvider apiKeyAuthenticationProvider(ApiKeyConfig config) {
        return new ApiKeyAuthenticationProvider(config);
    }

    @Bean(name = "basicAuthProvider")
    AuthenticationProvider basicAuthAuthenticationProvider(BasicAuthConfig config) {
        return new BasicAuthAuthenticationProvider(config);
    }

    @Bean
    ReactiveAuthenticationManager manager(
            @Qualifier("basicAuthProvider") AuthenticationProvider basicAuth,
            @Qualifier("apiKeyAuthProvider") AuthenticationProvider apiKeyAuth
    ) {
        return new AuthenticationManager(new ArrayList<AuthenticationProvider>() {{
            add(basicAuth);
            add(apiKeyAuth);
        }});
    }
}
