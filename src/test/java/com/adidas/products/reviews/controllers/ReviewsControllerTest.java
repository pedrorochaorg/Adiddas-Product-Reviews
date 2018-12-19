package com.adidas.products.reviews.controllers;

import static java.lang.Thread.sleep;

import com.adidas.products.reviews.AbstractTestRunner;
import com.adidas.products.reviews.common.messages.Rest;
import com.adidas.products.reviews.config.ApiKeyConfig;
import com.adidas.products.reviews.config.BasicAuthConfig;
import com.adidas.products.reviews.models.ErrorResponse;
import com.adidas.products.reviews.models.ProductScore;
import com.adidas.products.reviews.models.Review;
import com.adidas.products.reviews.repositories.ReviewRepository;
import com.adidas.products.reviews.services.ReviewService;
import com.adidas.products.reviews.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Reviews Rest Controller integration test.
 *
 * @author pedrorocha
 **/
public class ReviewsControllerTest extends AbstractTestRunner {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BasicAuthConfig basicAuthConfig;

    @Autowired
    ApiKeyConfig apiKeyConfig;

    @Autowired
    ReviewService reviewService;

    @Test(groups = {"integration", "reviews_rest_first"})
    public void getAllReviews_ifThereAreNoReviews_ReturnAnEmptySet() {


        webTestClient.get().uri("/review")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Review.class).hasSize(0);

    }


    @Test(groups = {"integration","reviews_rest_second"},dependsOnGroups = "reviews_rest_first")
    public void getAllReviews_ifThereAreReviews_ReturnTheReviews() {


        Flux<Review> reviewsFlux = this.createReviews();
        List<Review> reviews = reviewsFlux.collectList().block();



        webTestClient.get().uri("/review")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Review.class).contains(reviews.toArray(new Review[0]));

        this.reviewRepository.deleteAll(reviews);

    }


    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void getAllReviews_ifThereAreReviewsThatMatchASpecificProductId_ReturnTheSameReviews() {


        Flux<Review> reviewsFlux = this.createReviews("P1", null);
        List<Review> reviews = reviewsFlux.collectList().block();
        reviews.remove(reviews.size()-1);


        webTestClient.get().uri("/review?productId=P1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Review.class).contains(reviews.toArray(new Review[0]));

        this.reviewRepository.deleteAll(reviews);

    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void getAllReviews_ifThereAreReviewsThatDoNotMatchASpecificProductId_ReturnAnEmptySet() {


        Flux<Review> reviewsFlux = this.createReviews();
        List<Review> reviews = reviewsFlux.collectList().block();



        webTestClient.get().uri("/review?productId=P3")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Review.class).doesNotContain(reviews.toArray(new Review[0]));

        this.reviewRepository.deleteAll(reviews);

    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void getReview_ifThReviewExists_HasToReturnTheReview() {


        Flux<Review> reviewsFlux = this.createReviews();
        List<Review> reviews = reviewsFlux.collectList().block();



        webTestClient.get().uri("/review/" + reviews.get(0).getProductId() + "/" + reviews.get(0).getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(Review.class).isEqualTo(reviews.get(0));

        this.reviewRepository.deleteAll(reviews);

    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void getReview_ifTheReviewDoesntExists_HasToReturn404NotFound() {


        webTestClient.get().uri("/review/XX/1231231312")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorResponse.class).isEqualTo(new ErrorResponse(Rest.OBJECT_NOT_FOUND));

    }


    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void getScore_ifReviewsExists_HasToReturnTheRightScore() throws InterruptedException {


        List<Review> reviews = this.createReviewsService();

        Double tmpScore = 0.0;
        int count = 0;
        for (Review review : reviews) {
            if (review.getProductId().equals(reviews.get(0).getProductId())) {
                tmpScore = tmpScore + review.getScore();
                count++;
            }
        }
        final Double score = tmpScore / (double)count;

        sleep(100);

        webTestClient.get().uri("/review/" + reviews.get(0).getProductId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ProductScore.class)
                .isEqualTo(
                        ProductScore.builder()
                                .productId(reviews.get(0).getProductId())
                                .numberOfReviews((long)count)
                                .averageReviewScore(score)
                                .build()
                );

        this.reviewRepository.deleteAll(reviews);

    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void getScore_ifNoReviewsExists_HasToReturnAnEmptyScore() {




        webTestClient.get().uri("/review/C123")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody(ProductScore.class)
                .isEqualTo(
                        ProductScore.builder()
                                .productId("C123")
                                .numberOfReviews((long)0)
                                .averageReviewScore(0.0)
                                .build()
                );



    }



    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void createReview_tryToCreateANewReviewWithouthAuth_HasToFailWithAnUnauthorizedBodyMessage() {
        Review review = Review.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .comment("This is a comment")
                .productId("C323")
                .score(4.5)
                .build();

        webTestClient.post().uri("/review/C323")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(review), Review.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ErrorResponse.class).isEqualTo(new ErrorResponse("Not Authenticated"));



    }


    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void createReview_tryToCreateANewReviewWithBasicAuth_HasToSucceed() {
        final Review review = Review.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .comment("This is a comment")
                .productId("C423")
                .score(4.5)
                .build();

        webTestClient.post().uri("/review/C423")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Basic "
                        + Base64.encode(basicAuthConfig.getBasicAuth().get(0).getUsername() + ":"
                        + basicAuthConfig.getBasicAuth().get(0).getPassword())
                )
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(review), Review.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class);

        reviewRepository.deleteAll();
    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void createReview_tryToCreateANewReviewWithApiKeyAuth_HasToSucceed() {
        final Review review = Review.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .comment("This is a comment")
                .productId("C423")
                .score(4.5)
                .build();

        webTestClient.post().uri("/review/C423")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "APIKEY "
                        + apiKeyConfig.getApiKeys().get(0).getKey()
                )
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(review), Review.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Review.class);

        reviewRepository.deleteAll();
    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void createReview_tryToCreateANewReviewWithFakeBasicAuth_HasToFail() {
        final Review review = Review.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .comment("This is a comment")
                .productId("C423")
                .score(4.5)
                .build();

        webTestClient.post().uri("/review/C423")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Basic "
                        + Base64.encode(basicAuthConfig.getBasicAuth().get(0).getUsername() + ":"
                        + "something")
                )
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(review), Review.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class).isEqualTo(new ErrorResponse(Rest.INVALID_AUTHENTICATION_CREDENTIALS));

        reviewRepository.deleteAll();
    }

    @Test(groups = {"integration"},dependsOnGroups = "reviews_rest_second")
    public void createReview_tryToCreateANewReviewWithFakeApiKeyAuth_HasToFail() {
        final Review review = Review.builder()
                .name("John Doe")
                .email("johndoe@example.com")
                .comment("This is a comment")
                .productId("C423")
                .score(4.5)
                .build();

        webTestClient.post().uri("/review/C423")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "APIKEY XXXXXXX"
                )
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(review), Review.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ErrorResponse.class).isEqualTo(new ErrorResponse(Rest.INVALID_AUTHENTICATION_CREDENTIALS));

        reviewRepository.deleteAll();
    }


    /**
     * Creates a set of review object to use in each test. 2 with the same product id and another
     * one with a different product id. These id's are randomly setted.
     *
     * @return List of Review objects
     */
    private List<Review> createReviewsService() {
        return this.createReviewsService(null, null);
    }

    /**
     * Creates a set of review object to use in each test. 2 with the same product id and another
     * one with a different product id.
     *
     * @param productId sample product id to match the review
     * @param secondaryProductId secondary sample product id
     * @return List of Review objects
     */
    private List<Review> createReviewsService(String productId, String secondaryProductId) {
        final String finalProductId
                = (productId == null) ? UUID.randomUUID().toString().substring(0,5) : productId;

        final String finalSecondaryProductId
                = (secondaryProductId == null) ? UUID.randomUUID().toString().substring(0,5) : secondaryProductId;


        List<Review> reviews = new ArrayList<Review>(){{
            add(new Review(
                    null,
                    finalProductId,
                    "John Doe",
                    "johndoe@example.com",
                    2.0,
                    "hello world",
                    null
            ));
            add(new Review(
                    null,
                    finalProductId,
                    "Jane Doe",
                    "janedoe@example.com",
                    3.0,
                    "hello world",
                    null
            ));
            add(new Review(
                    null,
                    finalSecondaryProductId,
                    "Jane Doe",
                    "janedoe@example.com",
                    4.0,
                    "hello world",
                    null
            ));
        }};
        return reviews.stream()
                .map(review -> this.reviewService.save(review))
                .map(Mono::block)
                .collect(Collectors.toList());
    }

    /**
     * Creates a set of review object to use in each test. 2 with the same product id and another
     * one with a different product id. These id's are randomly setted.
     *
     * @return Flux of Review objects
     */
    private Flux<Review> createReviews() {
        return this.createReviews(null, null);
    }

    /**
     * Creates a set of review object to use in each test. 2 with the same product id and another
     * one with a different product id.
     *
     * @param productId sample product id to match the review
     * @param secondaryProductId secondary sample product id
     * @return Flux of Review objects
     */
    private Flux<Review> createReviews(String productId, String secondaryProductId) {
        final String finalProductId
                = (productId == null) ? UUID.randomUUID().toString().substring(0,5) : productId;

        final String finalSecondaryProductId
                = (secondaryProductId == null) ? UUID.randomUUID().toString().substring(0,5) : secondaryProductId;


        List<Review> reviews = new ArrayList<Review>(){{
            add(new Review(
                    null,
                    finalProductId,
                    "John Doe",
                    "johndoe@example.com",
                    2.0,
                    "hello world",
                    null
            ));
            add(new Review(
                    null,
                    finalProductId,
                    "Jane Doe",
                    "janedoe@example.com",
                    3.0,
                    "hello world",
                    null
            ));
            add(new Review(
                    null,
                    finalSecondaryProductId,
                    "Jane Doe",
                    "janedoe@example.com",
                    4.0,
                    "hello world",
                    null
            ));
        }};

        return reviewRepository.saveAll(reviews);
   }
}
