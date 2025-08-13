package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.Product;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.repository.ProductRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class WatchListService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void addNewProductToWatchList(String username, int productId) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        Optional<Product> productOptional = productRepository.getProductById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        User user = userOptional.get();
        Product product = productOptional.get();
        user.addProductToWatchList(product);
        userRepository.updateUser(user);
    }

    public void removeProductFromWatchList(String username, int productId) {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        Optional<Product> productOptional = productRepository.getProductById(productId);
        if (!productOptional.isPresent()) {
            throw new RuntimeException("Product not found");
        }
        User user = userOptional.get();
        Product product = productOptional.get();
        user.removeProductFromWatchList(product);
        userRepository.updateUser(user);
    }

    public List<Product> getAllProductsFromWatchList(String username) {
        Optional<User> userOptional = userRepository.loadUserByUsernameWithWatchList(username);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found");
        }

        return userOptional.get().getWatchlist();
    }
}
