package com.kickrank.config;

import com.kickrank.common.enums.Role;
import com.kickrank.user.entity.User;
import com.kickrank.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedAdmin(
            @Value("${app.seed.admin-email:admin@kickrank.local}") String email,
            @Value("${app.seed.admin-password:Admin12345}") String password
    ) {
        return args -> {
            if (userRepository.existsByEmail(email.toLowerCase())) {
                return;
            }
            var admin = new User();
            admin.setFullName("KickRank Admin");
            admin.setEmail(email.toLowerCase());
            admin.setPasswordHash(passwordEncoder.encode(password));
            admin.setRoles(Set.of(Role.PLAYER, Role.ADMIN, Role.ORGANIZER, Role.OBSERVER));
            admin.setOrganizerVerified(true);
            admin.setObserverVerified(true);
            userRepository.save(admin);
        };
    }
}
