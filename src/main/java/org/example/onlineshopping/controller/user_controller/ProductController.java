package org.example.onlineshopping.controller.user_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.response.ProductResponse;
import org.example.onlineshopping.entity.OrderItem;
import org.example.onlineshopping.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) throws Exception {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/recent/{top-n}")
    public ResponseEntity<List<OrderItem>> getMostRecentlyPurchasedProducts(@PathVariable(name = "top-n") int topN, Principal principal) {
        String username = "user";
        List<OrderItem> orderItems = productService.getMostRecentlyPurchasedProducts(topN, username);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/frequent/{top-n}")
    public ResponseEntity<List<OrderItem>> getMostFrequentlyPurchasedProducts(@PathVariable(name = "top-n") int topN, Principal principal) {
        String username = "user";
        List<OrderItem> orderItems = productService.getMostFrequentlyPurchasedProducts(topN, username);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
}
