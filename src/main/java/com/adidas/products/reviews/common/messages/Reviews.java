package com.adidas.products.reviews.common.messages;

/**
 * Set of static vars, that store common messages used for review operations.
 *
 * @author pedrorocha
 **/
public final class Reviews {

    public static final String INVALID_NAME = "The value of the property name is invalid.";
    public static final String INVALID_EMAIL = "The value of the property email is invalid.";
    public static final String INVALID_SCORE = "The value of the property score is invalid.";
    public static final String INVALID_COMMENT = "The value of the property comment is invalid.";
    public static final String INVALID_PRODUCT_ID = "The value of the property productId is invalid.";

    public static final String NOT_FOUND_EXCEPTION = "The review {id} hasn't been found on our database.";
    public static final String DUPLICATE_VALUE_EXCEPTION
            = "A Review with the same content already exists on our database.";

    private Reviews(){

    }
}
