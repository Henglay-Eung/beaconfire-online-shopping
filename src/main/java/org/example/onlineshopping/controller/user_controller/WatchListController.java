package org.example.onlineshopping.controller.user_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.response.BaseApiResponse;
import org.example.onlineshopping.domain.response.ProductResponse;
import org.example.onlineshopping.service.WatchListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchListController {
    private final WatchListService watchListService;

    @PostMapping("/product/{id}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> AddProductToWatchlist(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        ProductResponse productResponse = watchListService.addNewProductToWatchList(username, id);
        return new ResponseEntity<>(new BaseApiResponse<>("Product added to watchlist successfully", productResponse), HttpStatus.CREATED);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<BaseApiResponse<ProductResponse>> removeProductFromWatchlist(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        ProductResponse productResponse = watchListService.removeProductFromWatchList(username, id);
        return new ResponseEntity<>(new BaseApiResponse<>("Product removed from watchlist successfully", productResponse), HttpStatus.CREATED);
    }

    @GetMapping("/products/all")
    public ResponseEntity<BaseApiResponse<List<ProductResponse>>> GetAllProductsFromWatchlist(Principal principal) {
        String username = principal.getName();
        List<ProductResponse> products = watchListService.getAllProductsFromWatchList(username);
        return new ResponseEntity<>(new BaseApiResponse<>("Watchlist products fetched successfully", products), HttpStatus.OK);
    }

}
