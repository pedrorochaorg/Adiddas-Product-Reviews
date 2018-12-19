package com.adidas.products.reviews.util;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

/**
 * Set of utility methods to encode/decode base64 auth strings.
 *
 * @author pedrorocha
 **/
@Slf4j
public final class Base64 {

    /**
     * Decodes a base64 string into a readable one.
     *
     * @param base64String base64 string content to decode
     * @return a decoded string
     */
    public static String decode(String base64String) {
        byte[] base64Token = base64String.getBytes(StandardCharsets.UTF_8);
        byte[] decoded;
        String token = "";
        try {
            decoded = java.util.Base64.getDecoder().decode(base64Token);
            token = new String(decoded, StandardCharsets.UTF_8);

        } catch (IllegalArgumentException ex) {
            log.debug("Exception: {}", ex.getMessage());
        }

        return token;
    }

    /**
     * Encodes a string into a base64 string.
     *
     * @param stringToEncode string to encode
     * @return base64 string
     */
    public static String encode(String stringToEncode) {

        byte[] base64String = stringToEncode.getBytes(StandardCharsets.UTF_8);

        try {
            return java.util.Base64.getEncoder().encodeToString(base64String);

        } catch (IllegalArgumentException ex) {
            log.debug("Exception: {}", ex.getMessage());
        }

        return null;
    }

}
