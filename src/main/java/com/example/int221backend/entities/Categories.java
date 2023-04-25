package com.example.int221backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Categories {
    @Id
    private int categoryId;
    private String categoryName;

    @JsonIgnore
    @OneToMany(mappedBy = "categoriesObject")
    private Set<Announces> announces = new LinkedHashSet<>();

}
