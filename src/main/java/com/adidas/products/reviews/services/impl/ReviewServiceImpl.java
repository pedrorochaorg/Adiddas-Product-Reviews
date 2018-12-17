package com.adidas.products.reviews.services.impl;

import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.repositories.ReviewRepository;
import com.adidas.products.reviews.services.ProductScoreService;
import com.adidas.products.reviews.services.ReviewService;
import javax.sound.sampled.ReverbType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ProductScoreService scoreService;

    /**
     * Instantiates this object injecting the required dependencies.
     * @param productScoreService instance of the product score service
     * @param reviewRepository instance of the review repository
     */
    public ReviewServiceImpl(ProductScoreService productScoreService, ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.scoreService = productScoreService;
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
        return this.reviewRepository.findByIdAndProductId(productId,id);
    }

    @Override
    public Mono<Review> save(final Review review) {
        Mono<Review> reviewMono = this.reviewRepository.save(review);
        reviewMono.subscribe(review1 -> this.scoreService.calculateScore(review1.getProductId()));
        return reviewMono;
    }

    @Override
    public Mono<Void> delete(final Review review) {

        Mono<Void> reviewMono = this.reviewRepository.delete(review);
        reviewMono.subscribe(review1 -> this.scoreService.calculateScore(review.getProductId()));
        return reviewMono;
    }
}
