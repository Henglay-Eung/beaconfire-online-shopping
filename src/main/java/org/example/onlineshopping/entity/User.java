package org.example.onlineshopping.entity;

import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    private String username;

    private String email;

    private String password;

//    @Column
//    @OneToMany
//    @JoinColumn(name = "fk_auth_id")
//    private List<String> authorities;

    @ManyToMany
    @JoinTable(
        name = "watchlist",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> productWatchlist = new ArrayList<>();

    public void addProductToWatchList(Product product) {
        productWatchlist.add(product);
    }
    public void removeProductFromWatchList(Product product) {
        productWatchlist.remove(product);
    }
}
