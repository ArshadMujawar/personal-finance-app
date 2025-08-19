package com.personalfinance.app.service;

import com.personalfinance.app.model.Category;
import com.personalfinance.app.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category createCategory(String name) {
        if (categoryRepository.existsByName(name)) {
            throw new RuntimeException("Category already exists: " + name);
        }
        Category category = new Category(name);
        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(Integer id, String name) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(name);
            return categoryRepository.save(existing);
        });
    }

    public boolean deleteCategory(Integer id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
}




