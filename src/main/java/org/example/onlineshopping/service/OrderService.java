package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.OrderRequest;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.entity.OrderItem;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.exception.NotEnoughInventoryException;
import org.example.onlineshopping.repository.OrderRepository;
import org.example.onlineshopping.repository.ProductRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public void placeAnOrder(OrderRequest orderRequest, String username) {
        Order order = new Order();
        Optional<User> user = userRepository.loadUserByUsername(username);
        if (!user.isPresent()) {
            throw new RuntimeException(String.format("User not found"));
        }
        order.setUser(user.get());
        order.setOrderStatus("Processing");

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream().map(orderItem -> {
            Optional<Product> productOptional = productRepository.getProductById(orderItem.getItemId());
            if (!productOptional.isPresent()) {
                throw new RuntimeException(String.format("Item id = %d not found", orderItem.getItemId()));
            }
            Product product = productOptional.get();
            if (product.getQuantity() < orderItem.getQuantity()) {
                throw new NotEnoughInventoryException(String.format("Item id = %d not enough quantity", orderItem.getItemId()));
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
    }

    public void cancelAnOrder(int id, String username) {
        Optional<User> user = userRepository.loadUserByUsername(username);
        if (!user.isPresent()) {
            throw new RuntimeException(String.format("User not found"));
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
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public Order getOrderByIdForAdmin(int id) {
        Optional<Order> orderOptional = orderRepository.getAnOrderByIdForAdmin(id);
        if (!orderOptional.isPresent()) {
            throw new RuntimeException("Order not found");
        }
        return orderOptional.get();
    }

    public void cancelAnOrderForAdmin(int id) {
        Optional<Order> orderOptional = orderRepository.getAnOrderByIdForAdmin(id);
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
    }

    public void completeAnOrderForAdmin(int id) {
        Optional<Order> orderOptional = orderRepository.getAnOrderByIdForAdmin(id);
        if (!orderOptional.isPresent()) {
            throw new RuntimeException(String.format("Order id = %d not found", id));
        }
        Order order = orderOptional.get();
        if (order.getOrderStatus().equals("Canceled")) {
            throw new RuntimeException(String.format("Order id = %d cannot be completed since it is already canceled", id));
        }
        order.setOrderStatus("Completed");
        orderRepository.updateAnOrder(order);
    }
}
