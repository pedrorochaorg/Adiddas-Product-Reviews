package com.adidas.products.reviews.repositories;

import com.adidas.products.reviews.models.Review;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Review mongodb repository interface, used to perform operations with Review objects over the application
 * mongodb database.
 *
 * @author pedrorocha
 **/
@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {

    @Query("{ 'productId': ?0}")
    Flux<Review> findByProductId(final String productId);

    @Query("{ 'productId': ?0, 'id': ?1}")
    Mono<Review> findByIdAndProductId(final String productId, final String id);


}
