package com.adidas.products.reviews.controllers;

import com.adidas.products.reviews.common.messages.Rest;
import com.adidas.products.reviews.models.ErrorResponse;
import com.adidas.products.reviews.models.ProductScore;
import com.adidas.products.reviews.models.Review;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author pedrorocha
 **/
@Api(
        value = "Reviews",
        description = "CRUD Operations on Review objects",
        tags = {"Products", "Reviews"}
)
public interface IReviewsController {

    @ApiOperation(
            value = "List all reviews",
            response = Flux.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = Rest.CONTENT_READY,
                    response = Review[].class
            ),
            @ApiResponse(code = 401, message = Rest.UNAUTHENTICATED, response = ErrorResponse.class),
            @ApiResponse(code = 403, message = Rest.UNAUTHORIZED, response = ErrorResponse.class),
            @ApiResponse(code = 500, message = Rest.INTERNAL_SERVER_ERROR, response = ErrorResponse.class)
    })
    @RequestMapping(path = "/review", method = RequestMethod.GET, produces = Rest.CONTENT_FORMAT)
    Flux<Review> getAllReviews(
            @ApiParam(required = false, value = "Product Unique identifier")
            @RequestParam(name = "productId", required = false) final String productId
    );

    @ApiOperation(
            value = "Get a single review",
            response = Mono.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = Rest.CONTENT_READY,
                    response = Review.class
            ),
            @ApiResponse(code = 401, message = Rest.UNAUTHENTICATED, response = ErrorResponse.class),
            @ApiResponse(code = 403, message = Rest.UNAUTHORIZED, response = ErrorResponse.class),
            @ApiResponse(code = 404, message = Rest.OBJECT_NOT_FOUND, response = ErrorResponse.class),
            @ApiResponse(code = 500, message = Rest.INTERNAL_SERVER_ERROR, response = ErrorResponse.class)
    })
    @RequestMapping(path = "/review/{product_id}/{id}", method = RequestMethod.GET, produces = Rest.CONTENT_FORMAT)
    Mono<ResponseEntity<Review>> getReview(
            @ApiParam(required = true, value = "Product Unique identifier")
            @PathVariable(name = "product_id") final String productId,
            @ApiParam(required = true, value = "Review Unique identifier")
            @PathVariable(name = "id") final String id
    );

    @ApiOperation(
            value = "Get a product's score information",
            response = Mono.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = Rest.CONTENT_READY,
                    response = ProductScore.class
            ),
            @ApiResponse(code = 401, message = Rest.UNAUTHENTICATED, response = ErrorResponse.class),
            @ApiResponse(code = 403, message = Rest.UNAUTHORIZED, response = ErrorResponse.class),
            @ApiResponse(code = 500, message = Rest.INTERNAL_SERVER_ERROR, response = ErrorResponse.class)
    })
    @RequestMapping(path = "/review/{product_id}", method = RequestMethod.GET, produces = Rest.CONTENT_FORMAT)
    Mono<ResponseEntity<ProductScore>> getScore(
            @ApiParam(required = true, value = "Product Unique identifier")
            @PathVariable(name = "product_id") final String productId
    );

    @ApiOperation(
            value = "Create a new product review",
            response = Mono.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = Rest.CREATED,
                    response = Review.class
            ),
            @ApiResponse(code = 401, message = Rest.UNAUTHENTICATED, response = ErrorResponse.class),
            @ApiResponse(code = 403, message = Rest.UNAUTHORIZED, response = ErrorResponse.class),
            @ApiResponse(code = 500, message = Rest.INTERNAL_SERVER_ERROR, response = ErrorResponse.class)
    })
    @RequestMapping(
            path = "/review/{product_id}",
            method = RequestMethod.POST,
            produces = Rest.CONTENT_FORMAT,
            consumes = Rest.CONTENT_FORMAT
    )
    Mono<Review> createReview(
            @ApiParam(required = true, value = "Product Unique identifier")
            @PathVariable(name = "product_id") final String productId,
            @Valid
            @RequestBody
            @ApiParam(required = true, value = "Review Object")
            Review review
    );

    @ApiOperation(
            value = "Update a existing product review",
            response = Mono.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = Rest.CREATED,
                    response = Review.class
            ),
            @ApiResponse(code = 401, message = Rest.UNAUTHENTICATED, response = ErrorResponse.class),
            @ApiResponse(code = 403, message = Rest.UNAUTHORIZED, response = ErrorResponse.class),
            @ApiResponse(code = 404, message = Rest.OBJECT_NOT_FOUND, response = ErrorResponse.class),
            @ApiResponse(code = 500, message = Rest.INTERNAL_SERVER_ERROR, response = ErrorResponse.class)
    })
    @RequestMapping(
            path = "/review/{product_id}/{id}",
            method = RequestMethod.PUT,
            produces = Rest.CONTENT_FORMAT,
            consumes = Rest.CONTENT_FORMAT
    )
    Mono<ResponseEntity<Review>> updateReview(
            @ApiParam(required = true, value = "Product Unique identifier")
            @PathVariable(name = "product_id") final String productId,
            @ApiParam(required = true, value = "Review Unique identifier")
            @PathVariable(name = "id") final String id,
            @Valid
            @RequestBody
            @ApiParam(required = true, value = "Review Object")
            Review review
    );


    @ApiOperation(
            value = "Delete a existing product review",
            response = Mono.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = Rest.CREATED,
                    response = ResponseEntity.class
            ),
            @ApiResponse(code = 401, message = Rest.UNAUTHENTICATED, response = ErrorResponse.class),
            @ApiResponse(code = 403, message = Rest.UNAUTHORIZED, response = ErrorResponse.class),
            @ApiResponse(code = 404, message = Rest.OBJECT_NOT_FOUND, response = ErrorResponse.class),
            @ApiResponse(code = 500, message = Rest.INTERNAL_SERVER_ERROR, response = ErrorResponse.class)
    })
    @RequestMapping(
            path = "/review/{product_id}/{id}",
            method = RequestMethod.DELETE,
            produces = Rest.CONTENT_FORMAT,
            consumes = Rest.CONTENT_FORMAT
    )
    Mono<ResponseEntity<Void>> deleteReview(
            @ApiParam(required = true, value = "Product Unique identifier")
            @PathVariable(name = "product_id") final String productId,
            @ApiParam(required = true, value = "Review Unique identifier")
            @PathVariable(name = "id") final String id
    );

}
