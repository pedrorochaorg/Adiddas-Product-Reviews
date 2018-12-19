package com.adidas.products.reviews.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.testng.annotations.Test;

/**
 * @author pedrorocha
 **/
public class ResponseTest {

    @Test
    public void replyWith_ifServerHttpResponseIsNull_returnNull() {
        assertNull(Response.replyWith(null, HttpStatus.ACCEPTED, new Object()), "Object isn't null");
    }

    @Test
    public void replyWith_ifHttpStatusIsNull_returnMonoPeak() {
        String className = Response
                .replyWith(new MockServerHttpResponse(), null, new Object())
                .getClass()
                .getSimpleName();
        assertEquals(className, "MonoPeek", "Object isn't a mono peak object");
    }

    @Test
    public void replyWith_ifObjectIsNull_returnMonoPeak() {
        String className = Response
                .replyWith(new MockServerHttpResponse(), HttpStatus.ACCEPTED, null)
                .getClass()
                .getSimpleName();
        assertEquals(className, "MonoPeek", "Object isn't a mono peak object");
    }

    @Test
    public void replyWith_ifAllFieldsAreNull_returnNull() {
        assertNull(Response.replyWith(null, null, null), "Object isn't null");
    }
}
