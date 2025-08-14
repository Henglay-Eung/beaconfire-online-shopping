package org.example.onlineshopping.controller.admin_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.response.BaseApiResponse;
import org.example.onlineshopping.domain.response.OrderDetailsResponse;
import org.example.onlineshopping.domain.response.Pagination;
import org.example.onlineshopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;
    // For admin
    @GetMapping("/all")
    public ResponseEntity<Pagination<List<OrderDetailsResponse>>> getAllOrders(@RequestParam(required = false, defaultValue = "1") int page,
                                                                               @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                                               @RequestParam(required = false, defaultValue = "orderId") String sortedBy,
                                                                               @RequestParam(required = false, defaultValue = "desc") String direction) {
        Pagination<List<OrderDetailsResponse>> orders = orderService.getAllOrders(page, pageSize, sortedBy, direction);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<OrderDetailsResponse>> getOrderById(@PathVariable int id) {
        OrderDetailsResponse order = orderService.getOrderByIdForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Order fetched successfully", order), HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BaseApiResponse<OrderDetailsResponse>> cancelAnOrderForAdmin(@PathVariable int id) {;
        OrderDetailsResponse orderDetailsResponse = orderService.cancelAnOrderForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Order canceled successfully", orderDetailsResponse), HttpStatus.ACCEPTED);
    }
    @PatchMapping("/{id}/complete")
    public ResponseEntity<BaseApiResponse<OrderDetailsResponse>> completeAnOrderForAdmin(@PathVariable int id) {
        OrderDetailsResponse orderDetailsResponse =orderService.completeAnOrderForAdmin(id);
        return new ResponseEntity<>(new BaseApiResponse<>("Order completed successfully", orderDetailsResponse), HttpStatus.ACCEPTED);
    }
}
