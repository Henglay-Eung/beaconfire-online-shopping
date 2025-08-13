package org.example.onlineshopping.domain.login.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class OrderResponse {
    private final int productId;
    private final String description;
    private final String name;
    private final double retailPrice;
    private final double wholesalePrice;
}
