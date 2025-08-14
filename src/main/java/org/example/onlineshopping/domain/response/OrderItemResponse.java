package org.example.onlineshopping.domain.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private int itemId;
    private int quantity;
    private double purchasePrice;
    private double wholesalePrice;
}
