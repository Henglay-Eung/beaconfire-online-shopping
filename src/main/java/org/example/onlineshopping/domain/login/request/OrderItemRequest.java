package org.example.onlineshopping.domain.login.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    @Min(1)
    private int productId;
    @Min(1)
    private int quantity;
}
