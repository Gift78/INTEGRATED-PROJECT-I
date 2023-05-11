package com.example.int221backend.controllers;

import com.example.int221backend.entities.Categories;
import com.example.int221backend.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin(origins= {"http://localhost:5173", "http://intproj22.sit.kmutt.ac.th", "http://127.0.0.1:5173"})
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public List<Categories> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{categoryId}")
    public Categories getCategoryById(@PathVariable Integer categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public Categories addCategory(@RequestBody Categories newCategory){
        return categoryService.addCategory(newCategory);
    }
}