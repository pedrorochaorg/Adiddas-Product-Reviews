package com.adidas.products.reviews.services.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.adidas.products.reviews.models.ProductScore;
import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.services.ProductScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implements the product score service methods based on its interface.
 *
 * @author pedrorocha
 **/
@Service
@Slf4j
public class ProductScoreServiceImpl implements ProductScoreService {

    private final MongoOperations operations;

    /**
     * Instantiates this object injecting the required dependencies.
     *
     * @param mongoOperations instance of the mongo operations object to execute queries in the db.
     */
    public ProductScoreServiceImpl(
            MongoOperations mongoOperations
    ) {
        this.operations = mongoOperations;
    }

    @Override
    public Mono<ProductScore> findById(String id) {


        AggregationResults<ProductScore> results = operations.aggregate(newAggregation(Review.class,
                match(where("productId").is(id)),
                group()
                        .count().as("numberOfReviews")
                        .avg("score").as("averageReviewScore")
        ), ProductScore.class);
        ProductScore score = results.getUniqueMappedResult();
        if (score != null) {
            score.setProductId(id);
            return Mono.just(score);
        }
        return Mono.just(ProductScore.builder().productId(id).averageReviewScore(0.0).numberOfReviews((long)0).build());

    }


}
