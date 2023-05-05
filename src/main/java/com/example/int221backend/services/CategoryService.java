package com.example.int221backend.services;

import com.example.int221backend.entities.Categories;
import com.example.int221backend.exceptions.CategoryNotFoundException;
import com.example.int221backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Categories getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
