package com.onlineassessment.config;

import com.onlineassessment.entity.User;
import com.onlineassessment.enums.RoleEnum;
import com.onlineassessment.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        String adminEmail = "admin@judgex.com";

        if (!userRepository.existsByEmail(adminEmail)) {

            User admin = new User();

            admin.setName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(
                    passwordEncoder.encode("admin123")
            );

            admin.setRole(RoleEnum.ADMIN.ADMIN);

            userRepository.save(admin);

            System.out.println("Admin user created successfully.");
        }
    }
}