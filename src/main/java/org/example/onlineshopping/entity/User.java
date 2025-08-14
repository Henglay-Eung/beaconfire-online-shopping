package org.example.onlineshopping.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long userId;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    @Column
    @OneToMany
    @JoinColumn(name = "fk_auth_id")
    private List<Permission> permissions;

    @ManyToMany
    @JoinTable(
        name = "watchlist",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> watchlist = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();

    public void addProductToWatchList(Product product) {
        watchlist.add(product);
    }
    public void removeProductFromWatchList(Product product) {
        watchlist.remove(product);
    }
}
