package org.example.onlineshopping.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    @Min(value = 1, message = "productId must be 1 or greater")
    private int productId;
    @Min(value = 1, message = "quantity must be 1 or greater")
    private int quantity;
}
