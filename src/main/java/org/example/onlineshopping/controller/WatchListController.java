package org.example.onlineshopping.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.service.UserService;
import org.example.onlineshopping.service.WatchListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchListController {
    private WatchListService watchListService;
    private UserService userService;

    @PostMapping("/product/{id}")
    public void AddProductToWatchlist(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        watchListService.addNewProductToWatchList(username, id);
    }

    @DeleteMapping("/product/{id}")
    public void removeProductFromWatchlist(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        watchListService.removeProductFromWatchList(username, id);
    }

    @GetMapping("/products/all")
    public ResponseEntity<List<Product>> GetAllProductsFromWatchlist(Principal principal) {
        String username = principal.getName();
        List<Product> products = watchListService.getAllProductsFromWatchList(username);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
