package org.example.onlineshopping.controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.OrderRequest;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<String> placeAnOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderService.placeAnOrder(orderRequest, authentication.getName());
        return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> cancelAnOrder(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        orderService.cancelAnOrder(id, authentication.getName());
        return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
    }

}
