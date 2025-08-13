package org.example.onlineshopping.domain.login.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class ProductRequest {
    private final String name;
    private final String description;
    private final Double wholesalePrice;
    private final Double retailPrice;
    private final Integer quantity;
}
