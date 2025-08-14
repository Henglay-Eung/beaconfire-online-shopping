package org.example.onlineshopping.controller.user_controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.response.BaseApiResponse;
import org.example.onlineshopping.domain.response.OrderItemResponse;
import org.example.onlineshopping.domain.response.ProductResponse;
import org.example.onlineshopping.security.Views;
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
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(new BaseApiResponse<>("Products fetched successfully", products), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> getProductById(@PathVariable int id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Product fetched successfully", product), HttpStatus.OK);
    }

    @GetMapping("/recent/{top-n}")
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getMostRecentlyPurchasedProducts(@PathVariable(name = "top-n") int topN, Principal principal) {
        String username = principal.getName();
        List<ProductResponse> orderItems = productService.getMostRecentlyPurchasedProducts(topN, username);
        return new ResponseEntity<>(new BaseApiResponse<>("Most recently purchased products fetched", orderItems), HttpStatus.OK);
    }

    @GetMapping("/frequent/{top-n}")
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getMostFrequentlyPurchasedProducts(@PathVariable(name = "top-n") int topN, Principal principal) {
        String username = principal.getName();
        List<ProductResponse> orderItems = productService.getMostFrequentlyPurchasedProducts(topN, username);
        return new ResponseEntity<>(new BaseApiResponse<>("Most frequently purchased products fetched", orderItems), HttpStatus.OK);
    }
}
