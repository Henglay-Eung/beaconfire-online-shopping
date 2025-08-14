package org.example.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminProductSoldResponse extends ProductResponse{
    private Long soldCount;
    public AdminProductSoldResponse(int productId, String name, String description, double retailPrice, double wholesalePrice, long quantity) {
        super(productId, name, description, retailPrice, wholesalePrice);
        this.soldCount = quantity;
    }

}