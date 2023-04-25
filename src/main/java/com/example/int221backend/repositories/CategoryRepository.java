package com.example.int221backend.repositories;

import com.example.int221backend.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categories,Integer> {
}
