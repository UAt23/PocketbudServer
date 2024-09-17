package com.pocketbud.pocketbud.repository;

import com.pocketbud.pocketbud.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

