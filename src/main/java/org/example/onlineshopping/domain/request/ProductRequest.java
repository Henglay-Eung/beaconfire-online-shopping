package org.example.onlineshopping.domain.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
@Builder
public class ProductRequest {
    @NotBlank(message = "name is required and must not be blank")
    private final String name;
    @NotBlank(message = "description is required and must not be empty")
    private final String description;
    @Positive(message = "wholesalePrice is required and must be positive")
    private final Double wholesalePrice;
    @Positive(message = "retailPrice is required must be positive")
    private final Double retailPrice;
    @Positive(message = "quantity is required and  must be positive")
    private final Integer quantity;
}
