package com.adidas.products.reviews.components;

import com.adidas.products.reviews.common.messages.Rest;
import com.adidas.products.reviews.models.ErrorResponse;
import com.adidas.products.reviews.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Handel an unauthorized authentication error.
 *
 * @author pedrorocha
 **/
@Component
@Slf4j
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException e) {
        log.debug("Unauthorized");
        return Mono.fromRunnable(() -> Response.replyWith(
                exchange.getResponse(),
                HttpStatus.UNAUTHORIZED,
                new ErrorResponse(Rest.UNAUTHORIZED)
        ));
    }
}