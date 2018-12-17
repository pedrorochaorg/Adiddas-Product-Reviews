package com.adidas.products.reviews.exceptions;

import com.adidas.products.reviews.common.messages.Reviews;

/**
 * Exception thrown by a rest controller when a review can't be found on database.
 *
 * @author pedrorocha
 **/
public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(String id) {
        super(Reviews.NOT_FOUND_EXCEPTION.replace("{id}", id));
    }

}
