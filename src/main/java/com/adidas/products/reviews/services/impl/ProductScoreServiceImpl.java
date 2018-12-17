package com.adidas.products.reviews.services.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.adidas.products.reviews.models.ProductScore;
import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.repositories.ProductScoreRepository;
import com.adidas.products.reviews.repositories.ReviewRepository;
import com.adidas.products.reviews.services.ProductScoreService;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implements the product score service methods based on its interface.
 *
 * @author pedrorocha
 **/
@Service
@Slf4j
public class ProductScoreServiceImpl implements ProductScoreService {

    private final ProductScoreRepository productScoreRepository;
    private final MongoOperations operations;

    /**
     * Instantiates this object injecting the required dependencies.
     *
     * @param productScoreRepository instance of the product score repository
     * @param mongoOperations instance of the mongo operations object to execute queries in the db.
     */
    public ProductScoreServiceImpl(
            ProductScoreRepository productScoreRepository,
            MongoOperations mongoOperations
    ) {
        this.productScoreRepository = productScoreRepository;
        this.operations = mongoOperations;
    }

    @Override
    public Mono<ProductScore> findById(String id) {

            return this.productScoreRepository
                    .findByProductId(id)
                    .defaultIfEmpty(ProductScore.builder()
                            .productId(id)
                            .numberOfReviews((long)0)
                            .averageReviewScore(0.0)
                            .build());

    }

    @Override
    @Async
    public void calculateScore(String id) {
        log.info("CalculateScore: {}",id);

        AggregationResults<ProductScore> results = operations.aggregate(newAggregation(Review.class,
                match(where("productId").is(id)),
                group()
                        .count().as("numberOfReviews")
                        .avg("score").as("averageReviewScore")
        ), ProductScore.class);
        ProductScore score = results.getUniqueMappedResult();
        if (score != null) {
            score.setProductId(id);
            this.productScoreRepository.findByProductId(id)
                    .defaultIfEmpty(score)
                    .subscribe(finalScore -> {
                        if (finalScore.getId() != null) {
                            log.debug("FinalScore Update: {}", finalScore);
                            finalScore.setNumberOfReviews(score.getNumberOfReviews());
                            finalScore.setAverageReviewScore(score.getAverageReviewScore());
                            this.productScoreRepository.save(finalScore)
                                    .doOnError(mapper -> log.debug("Error Saving Score: {}", mapper.getMessage()))
                                    .doOnSuccess(mapper -> log.debug("Success Saving Score: {}", mapper.toString()))
                                    .subscribe(savedScore -> log.debug("Saved Score: {}", score));
                        } else {
                            log.debug("FinalScore Save: {}", finalScore);
                            this.productScoreRepository.save(finalScore)
                                    .doOnError(mapper -> log.debug("Error Saving Score: {}", mapper.getMessage()))
                                    .doOnSuccess(mapper -> log.debug("Success Saving Score: {}", mapper.toString()))
                                    .subscribe(savedScore -> log.debug("Saved Score: {}", score));
                        }
                    });


        }

    }
}
