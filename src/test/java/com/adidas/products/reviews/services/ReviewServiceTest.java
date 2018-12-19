package com.adidas.products.reviews.services;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.repositories.ReviewRepository;
import com.adidas.products.reviews.services.impl.ReviewServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author pedrorocha
 **/
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    private ReviewServiceImpl reviewService;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
        reviewService = new ReviewServiceImpl(reviewRepository);
    }

    @Test(groups = {"unit"})
    public void findById_IfTheresAMatchInDocumentStore_aReviewWithAMatchingIdIsReturned() {
        final String id = UUID.randomUUID().toString();
        Mockito.when(reviewRepository.findById(id)).thenReturn(Mono.just(Review.builder().id(id).build()));

        Mono<Review> review = reviewService.findById(id);

        StepVerifier
                .create(review)
                .assertNext(reviewReturned -> assertEquals(id, reviewReturned.getId()))
                .expectComplete()
                .verify();


    }

    @Test(groups = {"unit"})
    public void findById_IfTheresNotAMatchInDocumentStore_anEmptylObjectIsReturned() {
        final String id = UUID.randomUUID().toString();
        Mockito.when(reviewRepository.findById(id)).thenReturn(Mono.empty());

        Mono<Review> review = reviewService.findById(id);

        StepVerifier
                .create(review)
                .verifyComplete();


    }

    @Test(groups = {"unit"})
    public void findAll_IfTheresNoRecordsReturned_anEmptylObjectIsReturned() {
        Mockito.when(reviewRepository.findAll()).thenReturn(Flux.empty());

        Flux<Review> review = reviewService.findAll();

        StepVerifier
                .create(review)
                .verifyComplete();

    }

    @Test(groups = {"unit"})
    public void findAll_IfThereAreRecords_allDocumentsAreReturned() {
        List<Review> reviews = new ArrayList<Review>() {{
            add(Review.builder().id(UUID.randomUUID().toString()).build());
            add(Review.builder().id(UUID.randomUUID().toString()).build());
        }};

        Mockito.when(reviewRepository.findAll())
                .thenReturn(Flux.just(reviews.toArray(new Review[0])));

        Flux<Review> review = reviewService.findAll();

        StepVerifier
                .create(review)
                .assertNext(review1 -> {
                    assertNull(review1.getName());
                    Review existingReview = reviews.stream()
                            .filter(item -> item.getId().equals(review1.getId()))
                            .findFirst()
                            .get();
                    reviews.remove(existingReview);
                    assertNotNull(existingReview);
                })
                .assertNext(review1 -> {
                    assertNull(review1.getName());
                    Review existingReview = reviews.stream()
                            .filter(item -> item.getId().equals(review1.getId()))
                            .findFirst()
                            .get();
                    reviews.remove(existingReview);
                    assertNotNull(existingReview);
                })
                .expectComplete()
                .verify();

        assertTrue(reviews.isEmpty());

    }
}
