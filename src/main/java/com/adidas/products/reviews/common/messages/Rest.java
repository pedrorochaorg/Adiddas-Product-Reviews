package com.adidas.products.reviews.common.messages;

/**
 * Set of static vars that store common messages and constraints used for rest operations.
 *
 * @author pedrorocha
 **/
public final class Rest {
    // Messages
    public static final String UNAUTHENTICATED = "You are not authorized to view this resource";
    public static final String UNAUTHORIZED = "You have not been granted access to this resource";
    public static final String CONTENT_READY = "Content Ready";
    public static final String CREATED = "Object created successfully";
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String INVALID_REQUEST = "Invalid Request";
    public static final String OBJECT_NOT_FOUND = "Object Not Found";
    public static final String INVALID_AUTHENTICATION_CREDENTIALS = "Invalid authentication credentials";
    public static final String INVALID_API_KEY = "Invalid api key";
    public static final String INVALID_BASIC_AUTH = "Invalid basic auth";

    // Constraints
    public static final String CONTENT_FORMAT = "application/json";

    private Rest(){

    }
}
