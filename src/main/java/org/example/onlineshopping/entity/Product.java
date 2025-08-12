package org.example.onlineshopping.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    private String description;

    private String name;

    private int quantity;

    @Column(name = "retail_price")
    private double retailPrice;

    @Column(name = "wholesale_price")
    private double wholeSalePrice;

    @ManyToMany(mappedBy = "productWatchlist")
    private List<User> productWatchList = new ArrayList<>();
}
