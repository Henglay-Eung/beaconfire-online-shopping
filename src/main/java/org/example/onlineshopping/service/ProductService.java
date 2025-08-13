package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.ProductRequest;
import org.example.onlineshopping.domain.login.response.ProductResponse;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.entity.OrderItem;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.repository.OrderRepository;
import org.example.onlineshopping.repository.ProductRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        return products.stream().map(product -> new ProductResponse(product.getProductId(), product.getDescription(), product.getName(), product.getRetailPrice(), product.getWholeSalePrice())).collect(Collectors.toList());
    }

    public ProductResponse getProductById(int id) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        Product product = productOptional.get();
        return new ProductResponse(product.getProductId(), product.getDescription(), product.getName(),
                product.getRetailPrice(), product.getWholeSalePrice());
    }

    public List<OrderItem> getMostRecentlyPurchasedProducts(int topN, String username) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        List<Integer> orderIdList = orderRepository.getTopNMostRecentOrderListByUserId(user.getUserId());
        return orderRepository.getOrderItemsByOrderId(orderIdList, topN);
    }

    public List<OrderItem> getMostFrequentlyPurchasedProducts(int topN, String username) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        List<Integer> orderIdList = orderRepository.getOrderListByUserId(user.getUserId());

        return orderRepository.getTopNFrequentlyPurchasedOrderItemsByOrderId(orderIdList, topN);
    }

    public void updateProductByIdForAdmin(int id, ProductRequest productRequest) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        Product product = productOptional.get();
        if (productRequest.getQuantity() != null) {
            product.setQuantity(productRequest.getQuantity());
        }
        if (productRequest.getDescription() != null) {
            product.setDescription(productRequest.getDescription());
        }
        if (productRequest.getRetailPrice() != null) {
            product.setRetailPrice(productRequest.getRetailPrice());
        }
        if (productRequest.getWholesalePrice() != null) {
            product.setWholeSalePrice(productRequest.getWholesalePrice());
        }
        if (productRequest.getName() != null) {
            product.setName(productRequest.getName());
        }

        productRepository.updateProductForAdmin(product);
    }

    public void addProductForAdmin(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .retailPrice(productRequest.getRetailPrice())
                .wholeSalePrice(productRequest.getWholesalePrice())
                .quantity(productRequest.getQuantity())
                .build();


        productRepository.addProductForAdmin(product);
    }

    public List<OrderItem> getMostProfitableProducts(int topN) {
        List<Integer> orderIdList = orderRepository.getAllCompletedOrders();
        return orderRepository.getTopNMostProfitableItems(orderIdList, topN);
    }

    public List<OrderItem> getMostPopularProducts(int topN) {
        List<Integer> orderIdList = orderRepository.getAllCompletedOrders();
        return orderRepository.getTopNMostPopularItems(orderIdList, topN);
    }
}
