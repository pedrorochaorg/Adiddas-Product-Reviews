package com.adidas.products.reviews.models;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * ProductScore model, used for marshaling and unmarshaling data from/to json while interacting with REST endpoints.
 *
 * @author pedrorocha
 **/
@ApiModel(description = "ProductScore Model Object, ")
@Data
@Builder
@Document(collection = "product_scores")
public class ProductScore implements Serializable {

    @Id
    @ApiModelProperty(
            dataType = "String",
            example = "C77154",
            name = "id",
            readOnly = true
    )
    @JsonPropertyDescription("Product unique identifier")
    private String id;

    @Indexed(name = "productId")
    @ApiModelProperty(
            dataType = "String",
            example = "C1235",
            name = "productId"
    )
    @JsonPropertyDescription("Product unique identifier")
    private String productId;

    @Min(value = 0)
    @ApiModelProperty(
            dataType = "Long",
            example = "12312313",
            name = "numberOfReviews",
            readOnly = true
    )
    @JsonPropertyDescription("Total number of product received reviews")
    private Long numberOfReviews;

    @Min(value = 0)
    @ApiModelProperty(
            dataType = "Double",
            example = "99.532353",
            name = "averageReviewScore",
            readOnly = true
    )
    @JsonPropertyDescription("Average review score")
    private Double averageReviewScore;

    /**
     * Simplified class constructor.
     */
    public ProductScore() {
    }

    /**
     * Instantiates a new ProductScrore object.
     *
     * @param id                 product score id
     * @param productId          product unique identifier
     * @param numberOfReviews    total number of reviews
     * @param averageReviewScore average review score
     */
    public ProductScore(
            String id,
            String productId,
            @Min(value = 0)
                    Long numberOfReviews,
            @Min(value = 0)
                    Double averageReviewScore
    ) {
        this.id = id;
        this.productId = productId;
        this.numberOfReviews = numberOfReviews;
        this.averageReviewScore = averageReviewScore;
    }
}
