package com.adidas.products.reviews.services.impl;

import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.repositories.ReviewRepository;
import com.adidas.products.reviews.services.ProductScoreService;
import com.adidas.products.reviews.services.ReviewService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implements the review service methods based on its interface.
 *
 * @author pedrorocha
 **/
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Instantiates this object injecting the required dependencies.
     *
     * @param reviewRepository    instance of the review repository
     */
    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Mono<Review> findById(String id) {
        return this.reviewRepository.findById(id);
    }

    @Override
    public Flux<Review> findAll() {
        return this.reviewRepository.findAll();
    }

    @Override
    public Flux<Review> findByProductId(String productId) {
        return this.reviewRepository.findByProductId(productId);
    }

    @Override
    public Mono<Review> findByIdAndProductId(String productId, String id) {
        return this.reviewRepository.findByIdAndProductId(productId, id);
    }

    @Override
    public Mono<Review> save(final Review review) {
        return this.reviewRepository.save(review);
    }

    @Override
    public Mono<Void> delete(final Review review) {

        return this.reviewRepository.delete(review);
    }
}
