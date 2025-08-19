package com.personalfinance.app.config;

import com.personalfinance.app.model.Category;
import com.personalfinance.app.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initCategories(CategoryRepository categoryRepository) {
        return args -> {
            List<String> defaults = Arrays.asList(
                    "Food", "Transport", "Shopping", "Entertainment",
                    "Bills", "Healthcare", "Education", "Other"
            );
            for (String name : defaults) {
                categoryRepository.findByName(name).orElseGet(() -> categoryRepository.save(new Category(name)));
            }
        };
    }
}




