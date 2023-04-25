package com.example.int221backend.repositories;

import com.example.int221backend.entities.Announces;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnounceRepository extends JpaRepository<Announces, Integer> {
}
