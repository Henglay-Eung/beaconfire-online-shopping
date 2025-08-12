package org.example.onlineshopping.domain.login.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    private int productId;
    private String description;
    private String name;
    private double retailPrice;
    private double wholesalePrice;
}
