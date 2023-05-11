package com.example.int221backend.controllers;


import com.example.int221backend.entities.Categories;
import com.example.int221backend.services.CategoryService;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public List<Categories> getAllCategories() {
        return categoryService.getAllCategories();
    }

        @GetMapping("/categories/{categoryId}")
    public Categories getCategoryById(@PathVariable Integer categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public Categories addCategory(@RequestBody Categories newCategory){
        return categoryService.addCategory(newCategory);
    }
}
