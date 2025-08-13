package org.example.onlineshopping.service;

import lombok.RequiredArgsConstructor;
import org.example.onlineshopping.domain.login.request.UserRequest;
import org.example.onlineshopping.entity.Permission;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.repository.UserRepository;
import org.example.onlineshopping.security.AuthUserDetail;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return new AuthUserDetail(user.getUsername(), user.getPassword(), user.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getValue()))
                .collect(Collectors.toList()));
    }

    @Transactional
    public void registerUser(UserRequest userRequest) {
        if (!userRepository.isUsernameOrEmailAvailable(userRequest.getUsername(), userRequest.getEmail())) {
            throw new RuntimeException("Username or password is taken");
        }
        List<Permission> permissionList = new ArrayList<>();
        // Default permission
        permissionList.add(Permission.builder().value("user").build());
        userRepository.registerUser(
                userRequest.getUsername(),
                userRequest.getEmail(),
                passwordEncoder.encode(userRequest.getPassword()),
                permissionList);
    }
}
