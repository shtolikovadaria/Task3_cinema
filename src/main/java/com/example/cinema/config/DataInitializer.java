package com.example.cinema.config;

import com.example.cinema.entity.AppUser;
import com.example.cinema.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    // Если пользователей нет, создаём admin / admin (пароль хранится в виде хеша)
    @Bean
    public CommandLineRunner createAdmin(AppUserRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new AppUser("admin", encoder.encode("admin")));
            }
        };
    }
}