package com.adidas.products.reviews.services;

import com.adidas.products.reviews.models.Review;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Review Service used as a bridge between the restController and the review repository, delegating dependant operations
 * to other services/repositories.
 *
 * @author pedrorocha
 **/
public interface ReviewService {

    /**
     * Returns a single review which id value match the value of the arg id.
     *
     * @param id review unique identifier
     * @return a Mono reactive object with a Review entity object.
     */
    Mono<Review> findById(String id);

    /**
     * Returns all reviews stored in the mongodb service.
     *
     * @return a Flux reactive object with a set of Review objects.
     */
    Flux<Review> findAll();

    /**
     * Returns a set of reviews related with the same productId.
     *
     * @param productId product unique identifier
     * @return a Flux reactive object with a set of Review objects.
     */
    Flux<Review> findByProductId(String productId);

    /**
     * Returns a single review which id and productId values match the values of the args id and productId,
     * respectively.
     *
     * @param productId product unique identifier
     * @param id review unique identifier
     * @return a Mono reactive object with a Review entity object.
     */
    Mono<Review> findByIdAndProductId(String productId, String id);


    /**
     * Saves an entry in the database.
     *
     * @param review review object
     * @return a Mono reactive object with a Review entity object.
     */
    Mono<Review> save(Review review);

    /**
     * Deletes an entry from database.
     *
     * @param review review object
     * @return a Mono reactive object with a Review entity object.
     */
    Mono<Void> delete(Review review);

}
