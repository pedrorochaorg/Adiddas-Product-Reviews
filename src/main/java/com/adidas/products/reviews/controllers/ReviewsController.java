package com.adidas.products.reviews.controllers;

import com.adidas.products.reviews.common.messages.Rest;
import com.adidas.products.reviews.models.ProductScore;
import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.services.ProductScoreService;
import com.adidas.products.reviews.services.ReviewService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reviews Service Rest Controller
 *
 * @author pedrorocha
 **/
@RestController
@Slf4j
public class ReviewsController implements IReviewsController {

    private final ReviewService reviewService;
    private final ProductScoreService productScoreService;

    /**
     * Instantiates a new ReviewsController, implementing all interaface methods.
     *
     * @param reviewService instance to the review service
     * @param productScoreService instance to the product score service
     */
    public ReviewsController(
            ReviewService reviewService,
            ProductScoreService productScoreService
    ) {
        this.reviewService = reviewService;
        this.productScoreService = productScoreService;
    }

    @Override
    @RequestMapping(path = "/review", method = RequestMethod.GET, produces = Rest.CONTENT_FORMAT)
    public Flux<Review> getAllReviews(@RequestParam(name = "productId", required = false) final String productId) {
        log.info("getAllReviews , productId: {}", productId);
        if (productId != null) {
            return reviewService.findByProductId(productId);
        }

        return reviewService.findAll();
    }

    @Override
    @RequestMapping(path = "/review/{product_id}/{id}", method = RequestMethod.GET, produces = Rest.CONTENT_FORMAT)
    public Mono<ResponseEntity<Review>> getReview(
            @PathVariable(name = "product_id", required = true) final String productId,
            @PathVariable(name = "id", required = true) final String id) {
        log.info("getReview , productId: {}, id: {}", productId, id);
        return reviewService.findByIdAndProductId(productId, id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(
                        ResponseEntity.notFound().build()
                );
    }

    @Override
    @RequestMapping(path = "/review/{product_id}", method = RequestMethod.GET, produces = Rest.CONTENT_FORMAT)
    public Mono<ResponseEntity<ProductScore>> getScore(@PathVariable(name = "product_id") final String productId) {
        log.info("getScore , productId: {}", productId);
        return productScoreService.findById(productId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(
                        ResponseEntity.notFound().build()
                );
    }

    @Override
    @RequestMapping(
            path = "/review/{product_id}",
            method = RequestMethod.POST,
            produces = Rest.CONTENT_FORMAT,
            consumes = Rest.CONTENT_FORMAT
    )
    @PreAuthorize("hasAuthority('ROLE_API_REVIEW_ADMIN') or hasAuthority('ROLE_USER')")
    public Mono<Review> createReview(
            @PathVariable(name = "product_id") final String productId,
            @Valid @RequestBody Review review
    ) {
        log.info("createReview , productId: {}, review: {}", productId, review.toString());
        return this.reviewService.save(review);
    }

    @Override
    @RequestMapping(
            path = "/review/{product_id}/{id}",
            method = RequestMethod.PUT,
            produces = Rest.CONTENT_FORMAT,
            consumes = Rest.CONTENT_FORMAT
    )
    @PreAuthorize("hasAuthority('ROLE_API_REVIEW_ADMIN') or hasAuthority('ROLE_USER')")
    public Mono<ResponseEntity<Review>> updateReview(
            @PathVariable(name = "product_id") final String productId,
            @PathVariable(name = "id") final String id,
            @Valid @RequestBody Review review
    ) {
        log.info("updateReview , productId: {}, id: {}, review: {}", productId, id, review.toString());
        return reviewService.findById(id)
                .flatMap(existingReview -> {
                    existingReview.setName(review.getName());
                    return reviewService.save(existingReview);
                })
                .map(updatedReview -> new ResponseEntity<>(updatedReview, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    @RequestMapping(
            path = "/review/{product_id}/{id}",
            method = RequestMethod.DELETE,
            produces = Rest.CONTENT_FORMAT,
            consumes = Rest.CONTENT_FORMAT
    )
    @PreAuthorize("hasAuthority('ROLE_API_REVIEW_ADMIN') or hasAuthority('ROLE_USER')")
    public Mono<ResponseEntity<Void>> deleteReview(
            @PathVariable(name = "product_id") final String productId,
            @PathVariable(name = "id") final String id
    ) {
        log.info("deleteReview , productId: {}, id: {}", productId, id);
        return reviewService.findById(id)
                .flatMap(existingReview ->
                        reviewService.delete(existingReview)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
