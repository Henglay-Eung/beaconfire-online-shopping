package org.example.onlineshopping.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.request.ProductRequest;
import org.example.onlineshopping.domain.request.ProductUpdateRequest;
import org.example.onlineshopping.domain.response.*;
import org.example.onlineshopping.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<BaseApiResponse<List<AdminProductResponse>>> getAllProductsForAdmin() {
        List<AdminProductResponse> products = productService.getAllProductsForAdmin();
        return new ResponseEntity<>(new BaseApiResponse<>("Products fetched successfully.", products), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<AdminProductResponse>> getProductByIdForAdmin(@PathVariable int id) {
        AdminProductResponse productResponse = productService.getProductByIdForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Product fetched successfully", productResponse), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseApiResponse<AdminProductResponse>> updateProductByIdForAdmin(@PathVariable int id, @Valid @RequestBody ProductUpdateRequest productRequest) {
        AdminProductResponse productResponse = productService.updateProductByIdForAdmin(id, productRequest);
        return new ResponseEntity<>(new BaseApiResponse<>("Product updated successfully", productResponse), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseApiResponse<AdminProductResponse>> addProductForAdmin(@Valid @RequestBody ProductRequest productRequest) {
        AdminProductResponse productResponse = productService.addProductForAdmin(productRequest);
        return new ResponseEntity<>(new BaseApiResponse<>("Product added successfully", productResponse), HttpStatus.CREATED);
    }

    @GetMapping("/profit/{top-n}")
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> getMostProfitableProducts(@PathVariable(name = "top-n") int topN) {
        List<ProductResponse> orderItems = productService.getMostProfitableProducts(topN);
        return new ResponseEntity<>(new BaseApiResponse<>("Most profitable products fetched", orderItems), HttpStatus.OK);
    }

    @GetMapping("/popular/{top-n}")
    public ResponseEntity<BaseApiResponse<List<AdminProductResponse>>> getMostPopularProducts(@PathVariable(name = "top-n") int topN) {
        List<AdminProductResponse> orderItems = productService.getMostPopularProducts(topN);
        return new ResponseEntity<>(new BaseApiResponse<>("Most popular products fetched", orderItems), HttpStatus.OK);
    }

    @GetMapping("/sold")
    public ResponseEntity<BaseApiResponse<List<AdminProductSoldResponse>>> getTotalNumberOfItemsSold() {
        List<AdminProductSoldResponse> orderItems = productService.getTotalOfItemsSold();
        return new ResponseEntity<>(new BaseApiResponse<>("Total number of products sold fetched products", orderItems), HttpStatus.OK);
    }
}
