package com.adidas.products.reviews.services;

import com.adidas.products.reviews.models.ProductScore;
import com.adidas.products.reviews.models.Review;
import java.util.concurrent.CompletableFuture;
import org.springframework.scheduling.annotation.Async;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Product Score Service used as a bridge between the restController and the product score repository, delegating
 * dependant operations to other services/repositories.
 *
 * @author pedrorocha
 **/
public interface ProductScoreService {

    /**
     * Returns a single product score which id value match the value of the arg id.
     *
     * @param id review unique identifier
     * @return a Mono reactive object with a Review entity object.
     */
    Mono<ProductScore> findById(String id);


}
