package org.example.onlineshopping.domain.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Builder
public class ProductUpdateRequest {
    @Size(min = 1, message = "Name must not be empty")
    private String name;

    @Size(min = 1, message = "Description must not be empty")
    private String description;

    @Positive(message = "Wholesale price must be positive")
    private Double wholesalePrice;

    @Positive(message = "Retail price must be positive")
    private Double retailPrice;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
