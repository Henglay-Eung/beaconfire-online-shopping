package org.example.onlineshopping.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;
    // For admin
    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable int id) {
        Order order = orderService.getOrderByIdForAdmin(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAnOrderForAdmin(@PathVariable int id) {;
        orderService.cancelAnOrderForAdmin(id);
        return new ResponseEntity<>("Order canceled successfully", HttpStatus.ACCEPTED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<String> completeAnOrderForAdmin(@PathVariable int id) {
        orderService.completeAnOrderForAdmin(id);
        return new ResponseEntity<>("Order completed successfully", HttpStatus.ACCEPTED);
    }
}
