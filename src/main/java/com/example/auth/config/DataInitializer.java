package com.example.auth.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.auth.model.Role;
import com.example.auth.model.User;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepo.save(Role.builder().name("ROLE_ADMIN").build()));

        if (userRepo.findByEmail("admin@tvp.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@tvp.com")
                    .passwordHash(encoder.encode("admin123"))
                    .role(adminRole)
                    .enabled(true)
                    .build();
            userRepo.save(admin);
            System.out.println("Superusuario admin@tvp.com creado");
        }
    }
}

