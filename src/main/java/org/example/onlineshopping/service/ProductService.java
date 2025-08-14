package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.request.ProductRequest;
import org.example.onlineshopping.domain.response.AdminProductResponse;
import org.example.onlineshopping.domain.response.OrderItemResponse;
import org.example.onlineshopping.domain.response.ProductResponse;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.exception.NotFoundException;
import org.example.onlineshopping.repository.OrderRepository;
import org.example.onlineshopping.repository.ProductRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public List<OrderItemResponse> getMostRecentlyPurchasedProducts(int topN, String username) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();

        List<Integer> orderIdList = orderRepository.getTopNMostRecentOrderListByUserId(user.getUserId());
        return orderRepository.getOrderItemsByOrderId(orderIdList, topN).stream().map(orderItem -> OrderItemResponse
                .builder()
                .purchasePrice(orderItem.getPurchasedPrice())
                .wholesalePrice(orderItem.getWholesalePrice())
                .itemId(orderItem.getItemId())
                .quantity(orderItem.getQuantity())
                .build()).collect(Collectors.toList());
    }

    public List<OrderItemResponse> getMostFrequentlyPurchasedProducts(int topN, String username) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new NotFoundException("User not found");
        }
        User user = userOptional.get();

        List<Integer> orderIdList = orderRepository.getOrderListByUserId(user.getUserId());
        List<Integer> topNOrderItemIdList = orderRepository.getTopNFrequentOrderItemIdList(orderIdList, topN);
        return orderRepository.getTopNFrequentlyPurchasedOrderItemsByOrderIds(topNOrderItemIdList).stream().map(orderItem -> OrderItemResponse
                .builder()
                .purchasePrice(orderItem.getPurchasedPrice())
                .wholesalePrice(orderItem.getWholesalePrice())
                .itemId(orderItem.getItemId())
                .quantity(orderItem.getQuantity())
                .build()).collect(Collectors.toList());
    }

    public AdminProductResponse updateProductByIdForAdmin(int id, ProductRequest productRequest) {
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

    public List<OrderItemResponse> getMostProfitableProducts(int topN) {
        List<Integer> completedOrderIds = orderRepository.getAllCompletedOrders();
        List<Integer> topProductIds = orderRepository.getTopNProfitableOrderItemIdList(completedOrderIds, topN);
        return orderRepository.topProfitableProductIdList(topProductIds, completedOrderIds)
                .stream().map(orderItem -> OrderItemResponse.builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public List<OrderItemResponse> getMostPopularProducts(int topN) {
        List<Integer> orderIdList = orderRepository.getAllCompletedOrders();
        List<Integer> topPopularOrderItemIdList = orderRepository.getTopNPopularOrderItemIdList(orderIdList, topN);
        return orderRepository.getTopNMostPopularItems(topPopularOrderItemIdList)
                .stream().map(orderItem -> OrderItemResponse.builder()
                        .itemId(orderItem.getItemId())
                        .purchasePrice(orderItem.getPurchasedPrice())
                        .wholesalePrice(orderItem.getWholesalePrice())
                        .quantity(orderItem.getQuantity())
                        .build())
                .collect(Collectors.toList());
    }

    public Long getTotalOfItemsSold() {
        List<Integer> orderIdList = orderRepository.getAllCompletedOrders();
        return orderRepository.getTotalOfItemsSold(orderIdList);
    }
}
