package org.example.onlineshopping.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.response.BaseApiResponse;
import org.example.onlineshopping.domain.login.response.OrderResponse;
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
    public ResponseEntity<BaseApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return new ResponseEntity<>(new BaseApiResponse<>("Orders fetched successfully", orders), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<OrderResponse>> getOrderById(@PathVariable int id) {
        OrderResponse order = orderService.getOrderByIdForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Order fetched successfully", order), HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BaseApiResponse<OrderResponse>> cancelAnOrderForAdmin(@PathVariable int id) {;
        OrderResponse orderResponse = orderService.cancelAnOrderForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Order canceled successfully", orderResponse), HttpStatus.ACCEPTED);
    }
    @PatchMapping("/{id}/complete")
    public ResponseEntity<BaseApiResponse<OrderResponse>> completeAnOrderForAdmin(@PathVariable int id) {
        OrderResponse orderResponse =orderService.completeAnOrderForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Order completed successfully", orderResponse), HttpStatus.ACCEPTED);
    }
}
