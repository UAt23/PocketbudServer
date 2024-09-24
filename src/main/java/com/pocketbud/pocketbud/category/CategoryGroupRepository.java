package com.pocketbud.pocketbud.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    // Additional query methods can be defined here if needed
}
