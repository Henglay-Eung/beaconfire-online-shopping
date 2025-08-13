package org.example.onlineshopping.controller.user_controller;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.example.onlineshopping.domain.login.request.OrderRequest;
import org.example.onlineshopping.domain.login.response.OrderResponse;
import org.example.onlineshopping.domain.login.response.ProductResponse;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(Principal principal) {
        List<Order> orders = orderService.getAllOrdersForUser("user");
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id, Principal principal) {
        Order order = orderService.getOrderByIdForUser(id, "user");
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> placeAnOrder(@Valid @RequestBody OrderRequest orderRequest, Principal principal) {
        orderService.placeAnOrder(orderRequest, "user");
        return new ResponseEntity<>("Order placed successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAnOrder(@PathVariable int id, Principal principal) {
        orderService.cancelAnOrder(id, "user");
        return new ResponseEntity<>("Order canceled successfully", HttpStatus.OK);
    }

}
