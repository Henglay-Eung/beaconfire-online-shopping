package org.example.onlineshopping.controller.user_controller;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.request.OrderRequest;
import org.example.onlineshopping.domain.response.BaseApiResponse;
import org.example.onlineshopping.domain.response.OrderDetailsResponse;
import org.example.onlineshopping.domain.response.OrderResponse;
import org.example.onlineshopping.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseApiResponse<List<OrderResponse>>> getAllOrders(@RequestParam(required = false, defaultValue = "1") int page,
                                                                     @RequestParam(required = false, defaultValue = "5") int pageSize,
                                                                     @RequestParam(required = false, defaultValue = "order_id") String sortedBy,
                                                                     @RequestParam(required = false, defaultValue = "desc") String direction,
                                                                                 Principal principal) {
        String username = principal.getName();
        List<OrderResponse> orders = orderService.getAllOrdersForUser(username);
        return new ResponseEntity<>(new BaseApiResponse<>("Orders fetch successfully", orders), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<BaseApiResponse<OrderDetailsResponse>> getOrderById(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        OrderDetailsResponse order = orderService.getOrderByIdForUser(id, username);
        return new ResponseEntity<>(new BaseApiResponse<>("Order fetch successfully", order), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<BaseApiResponse<OrderDetailsResponse>> placeAnOrder(@Valid @RequestBody OrderRequest orderRequest, Principal principal) {
        String username = principal.getName();
        OrderDetailsResponse order = orderService.placeAnOrder(orderRequest, username);
        return new ResponseEntity<>(new BaseApiResponse<>("Order placed successfully", order), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BaseApiResponse<OrderDetailsResponse>> cancelAnOrder(@PathVariable int id, Principal principal) {
        String username = principal.getName();
        OrderDetailsResponse order = orderService.cancelAnOrder(id, username);
        return new ResponseEntity<>(new BaseApiResponse<>("Order canceled successfully", order), HttpStatus.OK);
    }

}
