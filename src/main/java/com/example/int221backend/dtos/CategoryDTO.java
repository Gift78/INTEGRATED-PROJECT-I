package com.example.int221backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }
}
