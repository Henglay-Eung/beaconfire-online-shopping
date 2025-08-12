package org.example.onlineshopping.domain.login.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class OrderItemRequest {
    @NotNull
    private final int itemId;
    @Min(1)
    private final int quantity;
}
