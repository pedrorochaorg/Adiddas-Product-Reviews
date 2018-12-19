package com.adidas.products.reviews.models;

import com.adidas.products.reviews.common.messages.Reviews;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Review model class, used to marshal/unmarshal data from/to mongodb and json. This model was based on the assumption
 * that every user can place a product review, if we require an user to be authenticated before posting a product
 * review, this model could be reviewed to include the user unique identifier as a required property.
 *
 * @author pedrorocha
 **/
@Data
@ApiModel(description = "Review Model Object")
@Document(collection = "reviews")
@Builder
@ToString
@EqualsAndHashCode
public class Review implements Serializable {

    @Id
    @ApiModelProperty(
            dataType = "String",
            example = "C77154",
            name = "id"
    )
    @JsonPropertyDescription("Review unique identifier")
    private String id;

    @Indexed(name = "productId")
    @ApiModelProperty(
            dataType = "String",
            example = "C1235",
            name = "productId"
    )
    @JsonPropertyDescription("Product unique identifier")
    private String productId;

    @NotBlank(message = Reviews.INVALID_NAME)
    @Size(max = 60, message = Reviews.INVALID_NAME)
    @ApiModelProperty(
            dataType = "String",
            example = "John Doe",
            name = "name",
            required = true
    )
    @Pattern(regexp = "^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = Reviews.INVALID_NAME)
    @JsonPropertyDescription("User name")
    private String name;

    @NotBlank(message = Reviews.INVALID_EMAIL)
    @Size(max = 90, message = Reviews.INVALID_EMAIL)
    @ApiModelProperty(
            dataType = "String",
            example = "john.doe@example.com",
            name = "email",
            required = true
    )
    @Email(message = Reviews.INVALID_EMAIL)
    @JsonPropertyDescription("User email address")
    private String email;

    @Indexed(name = "score")
    @NotNull(message = Reviews.INVALID_SCORE)
    @ApiModelProperty(
            dataType = "Decimal",
            example = "4.0",
            name = "score",
            required = true
    )
    @DecimalMin(value = "0.0", message = Reviews.INVALID_SCORE)
    @DecimalMax(value = "5.0", message = Reviews.INVALID_SCORE)
    @JsonPropertyDescription("Review score")
    private Double score;


    @Size(max = 270, message = Reviews.INVALID_COMMENT)
    @ApiModelProperty(
            dataType = "String",
            example = "I've loved this product, sure i would buy it again",
            name = "comment",
            required = true
    )
    @JsonPropertyDescription("Comment made by the reviewer")
    private String comment;


    @ApiModelProperty(
            dataType = "Date",
            name = "createdAt",
            readOnly = true
    )
    @Builder.Default
    private Date createdAt = new Date();


    /**
     * Simplified object constructor.
     */
    public Review() {
    }

    /**
     * Instantiates a new Review object.
     *
     * @param id        review unique identifier
     * @param productId product identifier
     * @param name      user name
     * @param email     user email
     * @param score     user voted score
     * @param comment   user comment
     * @param createdAt date of review
     */
    public Review(
            String id,
            @NotBlank(message = Reviews.INVALID_PRODUCT_ID)
                    String productId,
            @NotBlank(message = Reviews.INVALID_NAME)
            @Size(max = 60, message = Reviews.INVALID_NAME)
            @Pattern(regexp = "^[a-zA-Z]+(([\',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = Reviews.INVALID_NAME)
                    String name,
            @NotBlank(message = Reviews.INVALID_EMAIL)
            @Size(max = 90, message = Reviews.INVALID_EMAIL)
            @Email(message = Reviews.INVALID_EMAIL)
                    String email,
            @NotNull(message = Reviews.INVALID_SCORE)
            @Min(value = 0, message = Reviews.INVALID_SCORE)
            @Max(value = 5, message = Reviews.INVALID_SCORE)
                    Double score,
            @Size(max = 270, message = Reviews.INVALID_COMMENT)
                    String comment,
            @NotNull
                    Date createdAt
    ) {


        this.id = id;
        this.productId = productId;
        this.name = name;
        this.email = email;
        this.score = score;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
