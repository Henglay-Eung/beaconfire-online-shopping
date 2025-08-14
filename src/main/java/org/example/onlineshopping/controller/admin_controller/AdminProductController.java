package org.example.onlineshopping.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.ProductRequest;
import org.example.onlineshopping.domain.login.response.BaseApiResponse;
import org.example.onlineshopping.domain.login.response.OrderItemResponse;
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

    @GetMapping
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getAllProductsForAdmin() {
        List<ProductResponse> products = productService.getAllProducts();
        return new ResponseEntity<>(new BaseApiResponse<>("Products fetched successfully.", products), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> getProductByIdForAdmin(@PathVariable int id) {
        ProductResponse productResponse = productService.getProductById(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Product fetched successfully", productResponse), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> updateProductByIdForAdmin(@PathVariable int id, @RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.updateProductByIdForAdmin(id, productRequest);
        return new ResponseEntity<>(new BaseApiResponse<>("Product updated successfully", productResponse), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseApiResponse<ProductResponse>> addProductForAdmin(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.addProductForAdmin(productRequest);
        return new ResponseEntity<>(new BaseApiResponse<>("Product added successfully", productResponse), HttpStatus.CREATED);
    }

    @GetMapping("/profit/{top-n}")
    public ResponseEntity<BaseApiResponse<List<OrderItemResponse>>> getMostProfitableProducts(@PathVariable(name = "top-n") int topN) {
        List<OrderItemResponse> orderItems = productService.getMostProfitableProducts(topN);
        return new ResponseEntity<>(new BaseApiResponse<>("Most profitable products fetched", orderItems), HttpStatus.OK);
    }

    @GetMapping("/popular/{top-n}")
    public ResponseEntity<BaseApiResponse<List<OrderItemResponse>>> getMostPopularProducts(@PathVariable(name = "top-n") int topN) {
        List<OrderItemResponse> orderItems = productService.getMostPopularProducts(topN);
        return new ResponseEntity<>(new BaseApiResponse<>("Most popular products fetched", orderItems), HttpStatus.OK);
    }

    @GetMapping("/sold")
    public ResponseEntity<BaseApiResponse<Long>> getTotalNumberOfItemsSold() {
        Long orderItems = productService.getTotalOfItemsSold();
        return new ResponseEntity<>(new BaseApiResponse<>("Total number of products sold fetched products", orderItems), HttpStatus.OK);
    }
}
