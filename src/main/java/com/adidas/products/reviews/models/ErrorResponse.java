package com.adidas.products.reviews.models;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import lombok.Data;

/**
 * ErrorResponse model, used for marshaling and unmarshaling data from/to json while interacting with REST endpoints.
 * This object is used to output error messages thrown by rest controllers.
 *
 * @author pedrorocha
 **/
@ApiModel(description = "ErrorResponse Model Object")
@Data
public class ErrorResponse implements Serializable {

    @Min(value = 0)
    @ApiModelProperty(
            dataType = "String",
            example = "An error as occurred",
            name = "message",
            readOnly = true
    )
    @JsonPropertyDescription("A Message flagging the error thrown by the rest controller.")
    private String message;

    @Min(value = 0)
    @ApiModelProperty(
            dataType = "String",
            example = "Invalid fields",
            name = "description",
            readOnly = true
    )
    @JsonPropertyDescription("The error description.")
    private String description;

    /**
     * Simplified class constructor.
     */
    public ErrorResponse() {
    }

    /**
     * Constructs a new error message object, leaving the description field with a null value
     *
     * @param message Error message
     */
    public ErrorResponse(@Min(value = 0) String message) {
        this.message = message;
    }

    /**
     * Constructs a new error message object.
     *
     * @param message Error message
     * @param description Error description
     */
    public ErrorResponse(@Min(value = 0) String message, @Min(value = 0) String description) {
        this.message = message;
        this.description = description;
    }
}
