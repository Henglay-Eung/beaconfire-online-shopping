package org.example.onlineshopping.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "purchased_price")
    private double purchasedPrice;

    private int quantity;

    @Column(name = "wholesale_price")
    private double wholesalePrice;

    @ManyToOne
    @JoinColumn(name = "fk_order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "fk_product_id")
    private Product product;
}
