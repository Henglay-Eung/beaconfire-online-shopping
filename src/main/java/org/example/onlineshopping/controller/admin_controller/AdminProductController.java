package org.example.onlineshopping.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.ProductRequest;
import org.example.onlineshopping.domain.login.response.ProductResponse;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.entity.OrderItem;
import org.example.onlineshopping.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProductsForAdmin() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/all/{id}")
    public ResponseEntity<ProductResponse> getProductByIdForAdmin(@PathVariable int id) {
        ProductResponse products = productService.getProductById(id);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("/all/{id}")
    public void updateProductByIdForAdmin(@PathVariable int id, @RequestBody ProductRequest productRequest) {
        productService.updateProductByIdForAdmin(id, productRequest);
    }

    @PostMapping("/all/")
    public void addProductForAdmin(@RequestBody ProductRequest productRequest) {
        productService.addProductForAdmin(productRequest);
    }

    @GetMapping("/profit/{top-n}")
    public ResponseEntity<List<OrderItem>> getMostProfitableProducts(@PathVariable(name = "top-n") int topN) {
        List<OrderItem> orderItems = productService.getMostProfitableProducts(topN);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/popular/{top-n}")
    public ResponseEntity<List<OrderItem>> getMostPopularProducts(@PathVariable(name = "top-n") int topN) {
        List<OrderItem> orderItems = productService.getMostPopularProducts(topN);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<OrderItem>> getMostPopularProducts() {
        List<OrderItem> orderItems = productService.getMostPopularProducts(0);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
}
