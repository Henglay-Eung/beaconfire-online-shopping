package org.example.onlineshopping.domain.login.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class OrderRequest {
    @NotNull
    private int userId;

    @NotEmpty
    private List<OrderItemRequest> orderItems;
}
