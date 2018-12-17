package com.adidas.products.reviews.util;

import com.adidas.products.reviews.common.messages.Rest;
import com.adidas.products.reviews.models.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * Set of utility methods to mainpulate server response objects.
 *
 * @author pedrorocha
 **/
@Slf4j
public final class Response {

    /**
     * Instantiate a new response object.
     */
    private Response() {
    }

    /**
     * Writes a new reply message into the response server object.
     *
     * @param response Server Http Response instance
     * @param statusCode HttpStatus code to use
     * @param object object to reply with
     * @param <T> Serializable object
     */
    public static <T> void replyWith(ServerHttpResponse response, HttpStatus statusCode, T object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            byte[] bytes = mapper.writeValueAsString(object).getBytes(StandardCharsets.UTF_8);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.setStatusCode(statusCode);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            response.writeWith(Mono.just(buffer));
        } catch (IOException ex) {
            log.debug("ReplyWith: {}", ex.getMessage());
        }
    }
}
