package org.example.onlineshopping.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderResponse {
    private final int orderId;
    private final Date datePlaced;
    private final String orderStatus;
}
