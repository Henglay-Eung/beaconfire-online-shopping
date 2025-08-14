package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.example.onlineshopping.domain.login.request.OrderItemRequest;
import org.example.onlineshopping.domain.login.request.OrderRequest;
import org.example.onlineshopping.domain.login.response.OrderItemResponse;
import org.example.onlineshopping.domain.login.response.OrderResponse;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.entity.OrderItem;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.exception.NotEnoughInventoryException;
import org.example.onlineshopping.exception.NotFoundException;
import org.example.onlineshopping.repository.OrderRepository;
import org.example.onlineshopping.repository.ProductRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderResponse placeAnOrder(OrderRequest orderRequest, String username) {
        Order order = new Order();
        Optional<User> user = userRepository.loadUserByUsername(username);
        if (!user.isPresent()) {
            throw new RuntimeException(String.format("User not found"));
        }
        order.setUser(user.get());
        order.setOrderStatus("Processing");

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(orderItem -> {
            Optional<Product> productOptional = productRepository.getProductById(orderItem.getProductId());
            if (!productOptional.isPresent()) {
                throw new RuntimeException(String.format("Product id = %d not found", orderItem.getProductId()));
            }
            Product product = productOptional.get();
            if (product.getQuantity() < orderItem.getQuantity()) {
                throw new NotEnoughInventoryException(String.format("Product id = %d not enough quantity", orderItem.getProductId()));
            }
            return OrderItem.builder()
                    .purchasedPrice(product.getRetailPrice())
                    .wholesalePrice(product.getWholeSalePrice())
                    .quantity(orderItem.getQuantity())
                    .order(order)
                    .product(product)
                    .build();

        }).collect(Collectors.toList());
        order.setOrderItemList(orderItems);
        order.setDatePlaced(java.util.Date.from(
                LocalDateTime.now()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        ));
        orderRepository.placeAnOrder(order);
        return OrderResponse.builder().orderStatus(order.getOrderStatus())
                .datePlaced(order.getDatePlaced())
                .orderId(order.getOrderId())
                .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                        .builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .build();
    }

    public OrderResponse cancelAnOrder(int id, String username) {
        Optional<User> user = userRepository.loadUserByUsername(username);
        if (!user.isPresent()) {
            throw new NotFoundException("User not found");
        }
        Optional<Order> orderOptional = orderRepository.getAnOrderByIdAndUserId(id, user.get().getUserId());
        if (!orderOptional.isPresent()) {
            throw new RuntimeException(String.format("Order id = %d not found", id));
        }
        Order order = orderOptional.get();
        if (order.getOrderStatus().equals("Completed")) {
            throw new RuntimeException(String.format("Order id = %d cannot be canceled since it is already completed", id));
        }
        order.setOrderStatus("Canceled");
        order.getOrderItemList().forEach(orderItem -> {
            int productQuantity = orderItem.getProduct().getQuantity();
            productQuantity += orderItem.getQuantity();
            orderItem.getProduct().setQuantity(productQuantity);
        });
        orderRepository.updateAnOrder(order);
        return OrderResponse.builder().orderId(order.getOrderId())
                .orderStatus(order.getOrderStatus())
                .datePlaced(order.getDatePlaced())
                .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                        .builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .build();
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.getAllOrders().stream().map(order -> OrderResponse.builder()
                .orderStatus(order.getOrderStatus())
                .orderId(order.getOrderId())
                .datePlaced(order.getDatePlaced())
                .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                        .builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .build()).collect(Collectors.toList());
    }

    public OrderResponse getOrderByIdForAdmin(int id) {
        Optional<Order> orderOptional = orderRepository.getAnOrderById(id);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException("Order not found");
        }
        Order order = orderOptional.get();
        return OrderResponse.builder().orderStatus(order.getOrderStatus())
                .orderId(order.getOrderId())
                .datePlaced(order.getDatePlaced())
                .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                        .builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .build();
    }

    public OrderResponse cancelAnOrderForAdmin(int id) {
        Optional<Order> orderOptional = orderRepository.getAnOrderById(id);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException(String.format("Order id = %d not found", id));
        }
        Order order = orderOptional.get();
        if (order.getOrderStatus().equals("Completed")) {
            throw new NotFoundException(String.format("Order id = %d cannot be canceled since it is already completed", id));
        }
        order.setOrderStatus("Canceled");
        order.getOrderItemList().forEach(orderItem -> {
            int productQuantity = orderItem.getProduct().getQuantity();
            productQuantity += orderItem.getQuantity();
            orderItem.getProduct().setQuantity(productQuantity);
        });
        orderRepository.updateAnOrder(order);
        return OrderResponse.builder().orderStatus(order.getOrderStatus())
                .orderId(order.getOrderId())
                .datePlaced(order.getDatePlaced())
                .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                        .builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .build();

    }

    public OrderResponse completeAnOrderForAdmin(int id) {
        Optional<Order> orderOptional = orderRepository.getAnOrderById(id);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException(String.format("Order id = %d not found", id));
        }
        Order order = orderOptional.get();
        if (order.getOrderStatus().equals("Canceled")) {
            throw new NotFoundException(String.format("Order id = %d cannot be completed since it is already canceled", id));
        }
        order.setOrderStatus("Completed");
        orderRepository.updateAnOrder(order);
        return OrderResponse.builder().orderStatus(order.getOrderStatus())
                .orderId(order.getOrderId())
                .datePlaced(order.getDatePlaced())
                .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                        .builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build()).collect(Collectors.toList())
                )
                .build();
    }

    public List<OrderResponse> getAllOrdersForUser(String username) {
        Optional<User> user = userRepository.loadUserByUsernameWithOrderList(username);
        if (!user.isPresent()) {
            throw new NotFoundException("User not found");
        }
        return user.get().getOrderList().stream().map(order -> OrderResponse.builder()
                        .orderId(order.getOrderId())
                        .orderStatus(order.getOrderStatus())
                        .datePlaced(order.getDatePlaced())
                        .orderItemResponses(order.getOrderItemList().stream().map(orderItem -> OrderItemResponse
                                .builder()
                                .itemId(orderItem.getItemId())
                                .purchasePrice(orderItem.getPurchasedPrice())
                                .wholesalePrice(orderItem.getWholesalePrice())
                                .quantity(orderItem.getQuantity())
                                .build()).collect(Collectors.toList())
                        )
                        .build())
                .collect(Collectors.toList());
    }
    public OrderResponse getOrderByIdForUser(int id, String username) {
        Optional<OrderResponse> order  = getAllOrdersForUser(username).stream().filter(o -> o.getOrderId() == id)
                .findFirst();
        if (!order.isPresent()) {
            throw new NotFoundException("Order not found");
        }
        return order.get();

    }
}
