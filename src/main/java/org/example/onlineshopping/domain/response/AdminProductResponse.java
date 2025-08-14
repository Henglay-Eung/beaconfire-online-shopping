package org.example.onlineshopping.domain.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class AdminProductResponse extends ProductResponse {
    private int quantity;
    public AdminProductResponse(int productId, String name, String description, double retailPrice, double wholesalePrice, int quantity) {
        super(productId, name, description, retailPrice, wholesalePrice);
        this.quantity = quantity;
    }

}
