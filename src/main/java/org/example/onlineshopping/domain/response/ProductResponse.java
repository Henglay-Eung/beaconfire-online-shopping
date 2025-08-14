package org.example.onlineshopping.domain.response;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.example.onlineshopping.security.Views;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private int productId;
    private String name;
    private String description;
    private double retailPrice;
    private double wholesalePrice;
}
