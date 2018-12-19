package com.adidas.products.reviews.components;

import com.adidas.products.reviews.common.messages.Rest;
import com.adidas.products.reviews.models.ErrorResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Global Exception handler.
 *
 * @author pedrorocha
 **/
@Component
@Order(-2)
@Slf4j
public class GlobalErrorExceptionHandler extends AbstractErrorWebExceptionHandler {

    /**
     * Instantiates a enw Global Error Exception handler , injecting the required class instances.
     * @param g golbal error attributes object
     * @param applicationContext application context
     * @param serverCodecConfigurer server codec
     */
    public GlobalErrorExceptionHandler(GlobalErrorAttributes g, ApplicationContext applicationContext,
                                          ServerCodecConfigurer serverCodecConfigurer) {
        super(g, new ResourceProperties(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * Customizes the error response with a model object of type ErrorResponse.
     *
     * @param request server http request
     * @return Mono server response
     */
    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);

        Throwable throwable = getError(request);
        String className = throwable.getClass().getSimpleName();

        log.debug("ClassName: {}", className);
        log.debug("ErrorMessage: {}", throwable.getMessage());
        switch (className) {
            case "WebExchangeBindException":
                log.info(((WebExchangeBindException)throwable).getFieldErrors().toString());
                List<FieldError> fieldErrors = ((WebExchangeBindException)throwable).getFieldErrors();
                return ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(
                                new ErrorResponse(Rest.INVALID_REQUEST, fieldErrors.stream()
                                        .map(fieldError -> fieldError.getDefaultMessage())
                                        .collect(Collectors.joining(","))
                                )
                        ));
            case "ResponseStatusException":
                return ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(
                                new ErrorResponse(Rest.INVALID_REQUEST)));
            case "AuthenticationCredentialsNotFoundException":
                return ServerResponse.status((HttpStatus)errorPropertiesMap.get("status"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(
                                new ErrorResponse(Rest.UNAUTHORIZED)));
            default:
                return ServerResponse.status((HttpStatus)errorPropertiesMap.get("status"))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(
                                new ErrorResponse(Rest.INVALID_REQUEST, (String)errorPropertiesMap.get("error"))));

        }
    }

}