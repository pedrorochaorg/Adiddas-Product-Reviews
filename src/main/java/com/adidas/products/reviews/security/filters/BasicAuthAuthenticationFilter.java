package com.adidas.products.reviews.security.filters;

import com.adidas.products.reviews.security.AuthenticationManager;
import com.adidas.products.reviews.security.models.ApiKeyAuthentication;
import com.adidas.products.reviews.security.models.BasicAuthAuthentication;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Api Key Authentication filter, catches requests that contain an Authorization header with a starting value of APIKEY,
 * an tries to authenticate the subject against a list of know authorities.
 *
 * @author pedrorocha
 **/
@Slf4j
public class BasicAuthAuthenticationFilter extends AuthenticationWebFilter {

    private final ServerAuthenticationEntryPoint entryPoint;
    private final ServerSecurityContextRepository repository;

    /**
     * Instantiates a new Api Authentication Filter, injecting the required dependencies.
     *
     * @param manager    ReactiveAuthenticationManager
     * @param repository ServerSecurityContextRepository
     * @param entryPoint ServerAuthenticationEntryPoint
     */
    public BasicAuthAuthenticationFilter(
            ReactiveAuthenticationManager manager,
            ServerSecurityContextRepository repository,
            ServerAuthenticationEntryPoint entryPoint
    ) {
        super(manager);
        log.debug("Init BasicAuthFilter");
        this.repository = repository;
        this.entryPoint = entryPoint;
        this.setRequiresAuthenticationMatcher(new BasicAuthExchangeMatcher());
        this.setServerAuthenticationConverter(new BasicAuthAuthenticationConverter());
        this.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(entryPoint));
    }

    /**
     * Api Key Authentication converter , converts request headers in authentication manager data.
     */
    @AllArgsConstructor
    private class BasicAuthAuthenticationConverter implements ServerAuthenticationConverter {
        private final String BASIC = "Basic ";
        private final Predicate<String> matchBearerLength
                = authValue -> authValue.length() > BASIC.length();
        private final Function<String, String> isolateBearerValue
                = authValue -> authValue.substring(BASIC.length(), authValue.length());

        @Override
        public Mono<Authentication> convert(ServerWebExchange exchange) {
            log.debug("Convert");

            return Mono.justOrEmpty(exchange)
                    .map(this::getKeyFromRequest)
                    .filter(Objects::nonNull)
                    .filter(matchBearerLength)
                    .map(isolateBearerValue)
                    .filter(token -> !StringUtils.isEmpty(token))
                    .map(BasicAuthAuthentication::new)
                    .map(auth -> (Authentication)auth)
                    .filter(Objects::nonNull);

        }

        private String getKeyFromRequest(ServerWebExchange exchange) {
            String key = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);
            return StringUtils.isEmpty(key) ? Strings.EMPTY : key;
        }
    }

    /**
     * Checks if a request is targeted to this filter.
     */
    private class BasicAuthExchangeMatcher implements ServerWebExchangeMatcher {
        @Override
        public Mono<MatchResult> matches(final ServerWebExchange exchange) {
            log.debug("Check For Header");

            Mono<ServerHttpRequest> request = Mono.just(exchange).map(ServerWebExchange::getRequest);
            /* Check for header "Authorization" */
            return request.map(ServerHttpRequest::getHeaders)
                    .filter(h -> h.containsKey(HttpHeaders.AUTHORIZATION)
                            && h.getFirst(HttpHeaders.AUTHORIZATION).startsWith("Basic")
                    )
                    .flatMap($ -> MatchResult.match())
                    .switchIfEmpty(MatchResult.notMatch());
        }
    }

}
