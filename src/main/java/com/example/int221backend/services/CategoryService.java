package com.example.int221backend.services;


import com.example.int221backend.entities.Categories;
import com.example.int221backend.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Categories> getAllCategory(){
        return categoryRepository.findAll();
    }
}
