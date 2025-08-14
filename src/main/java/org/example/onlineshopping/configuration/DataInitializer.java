package org.example.onlineshopping.configuration;

import org.example.onlineshopping.entity.Permission;
import org.example.onlineshopping.entity.User;
import org.example.onlineshopping.repository.PermissionRepository;
import org.example.onlineshopping.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class DataInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PermissionRepository permissionRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (permissionRepository.countPermission() == 0) {
            permissionRepository.addPermission(Permission.builder().value("user").build());
            permissionRepository.addPermission(Permission.builder().value("admin").build());

                List<Permission> permissions = new ArrayList<>();
                Permission adminPermission = permissionRepository.getPermission("admin");
                permissions.add(adminPermission);
                userRepository.registerUser("admin", "admin@admin.com", passwordEncoder.encode("123"), permissions);

        }


    }
}
