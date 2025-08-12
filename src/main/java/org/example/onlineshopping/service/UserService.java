package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.repository.UserRepository;
import org.example.onlineshopping.security.AuthUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.loadUserByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = userOptional.get();
        return new AuthUserDetail(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    @Transactional
    public void registerUser(String username, String email, String password) throws Exception {
        if (!userRepository.isUsernameOrEmailAvailable(username, email)) {
            throw new Exception("Username or password taken");
        }
        userRepository.registerUser(username, email, passwordEncoder.encode(password));
    }
}
