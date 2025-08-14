package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.request.ProductRequest;
import org.example.onlineshopping.domain.request.ProductUpdateRequest;
import org.example.onlineshopping.domain.response.AdminProductResponse;
import org.example.onlineshopping.domain.response.AdminProductSoldResponse;
import org.example.onlineshopping.domain.response.OrderItemResponse;
import org.example.onlineshopping.domain.response.ProductResponse;
import org.example.onlineshopping.entity.Order;
import org.example.onlineshopping.entity.OrderItem;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.exception.NotFoundException;
import org.example.onlineshopping.repository.OrderRepository;
import org.example.onlineshopping.repository.ProductRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<AdminProductResponse> getAllProductsForAdmin() {
        List<Product> products = productRepository.getAllProducts();
        return products.stream().map(product -> new AdminProductResponse(product.getProductId(), product.getName(), product.getDescription(), product.getRetailPrice(), product.getWholeSalePrice(), product.getQuantity())).collect(Collectors.toList());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.getAllProducts();
        return products.stream().map(product -> new ProductResponse(product.getProductId(), product.getName(), product.getDescription(), product.getRetailPrice(), product.getWholeSalePrice())).collect(Collectors.toList());
    }

    public AdminProductResponse getProductByIdForAdmin(int id) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product not found");
        }
        Product product = productOptional.get();
        return new AdminProductResponse(product.getProductId(), product.getDescription(), product.getName(),
                product.getRetailPrice(), product.getWholeSalePrice(), product.getQuantity());
    }


    public ProductResponse getProductById(int id) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product not found");
        }
        Product product = productOptional.get();
        return new ProductResponse(product.getProductId(), product.getDescription(), product.getName(),
                product.getRetailPrice(), product.getWholeSalePrice());
    }

    public List<ProductResponse> getMostRecentlyPurchasedProducts(int topN, String username) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();

        List<Order> orderList = orderRepository.getMostRecentOrderListByUserId(user.getUserId());
        List<OrderItem> orderItemList = new ArrayList<>();

        for (int i = 0; i < orderList.size() && topN > 0; i++) {
            Order order = orderList.get(i);
            List<OrderItem> orderItems = order.getOrderItemList();
            orderItems.sort((a, b) -> b.getItemId() - a.getItemId());
            for (int j = 0; j < orderItems.size() && topN > 0; j++) {
                orderItemList.add(orderItems.get(j));
                topN--;
            }
        }

        return orderItemList.stream().map(orderItem -> ProductResponse
                .builder()
                .retailPrice(orderItem.getPurchasedPrice())
                .wholesalePrice(orderItem.getWholesalePrice())
                .productId(orderItem.getProduct().getProductId())
                .name(orderItem.getProduct().getName())
                .description(orderItem.getProduct().getDescription())
                .build()).collect(Collectors.toList());
    }

    public List<ProductResponse> getMostFrequentlyPurchasedProducts(int topN, String username) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();

        List<Integer> orderIdList = orderRepository.getOrderListByUserId(user.getUserId());
        List<Product> topNOrderItemIdList = orderRepository.getTopNFrequentOrderItemIdList(orderIdList, topN);
        return topNOrderItemIdList.stream().map(product -> ProductResponse
                .builder()
                .retailPrice(product.getRetailPrice())
                .wholesalePrice(product.getWholeSalePrice())
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .build()).collect(Collectors.toList());
    }

    public AdminProductResponse updateProductByIdForAdmin(int id, ProductUpdateRequest productRequest) {
        Optional<Product> productOptional = productRepository.getProductById(id);
        if (!productOptional.isPresent()) {
            throw new NotFoundException("Product not found");
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
        return new AdminProductResponse(product.getProductId(), product.getDescription(), product.getName(),
                product.getRetailPrice(), product.getWholeSalePrice(), product.getQuantity());
    }

    public AdminProductResponse addProductForAdmin(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .retailPrice(productRequest.getRetailPrice())
                .wholeSalePrice(productRequest.getWholesalePrice())
                .quantity(productRequest.getQuantity())
                .build();

        productRepository.addProductForAdmin(product);
        return new AdminProductResponse(product.getProductId(), product.getDescription(), product.getName(),
                product.getRetailPrice(), product.getWholeSalePrice(), product.getQuantity());
    }

    public List<ProductResponse> getMostProfitableProducts(int topN) {
        List<Integer> completedOrderIds = orderRepository.getAllCompletedOrders();
        List<Product> topProducts = orderRepository.getTopNProfitableOrderItemIdList(completedOrderIds, topN);
        return topProducts.stream().map(product -> new AdminProductResponse(product.getProductId(), product.getDescription(), product.getName(),
                        product.getRetailPrice(), product.getWholeSalePrice(), product.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<AdminProductResponse> getMostPopularProducts(int topN) {
        List<Integer> orderIdList = orderRepository.getAllCompletedOrders();
        List<Integer> topPopularOrderItemIdList = orderRepository.getTopNPopularOrderItemIdList(orderIdList, topN);
        return orderRepository.getTopNMostPopularItems(topPopularOrderItemIdList)
                .stream().map(orderItem -> new AdminProductResponse(orderItem.getProduct().getProductId(), orderItem.getProduct().getDescription(), orderItem.getProduct().getName(),
                        orderItem.getProduct().getRetailPrice(), orderItem.getProduct().getWholeSalePrice(), orderItem.getProduct().getQuantity()))
                .collect(Collectors.toList());
    }

    public List<AdminProductSoldResponse> getTotalOfItemsSold() {
        List<Integer> orderIdList = orderRepository.getAllCompletedOrders();
        List<Object[]> topPopularOrderItemIdList = orderRepository.getTopNPopularOrderItemIdList(orderIdList);
        return topPopularOrderItemIdList.stream().map(objects -> {
            Product product = (Product) objects[0];
            long quantity = (long) objects[1];
            return new AdminProductSoldResponse(product.getProductId(), product.getName(), product.getDescription(),
                    product.getRetailPrice(), product.getWholeSalePrice(), quantity);
        }).collect(Collectors.toList());
    }
}
