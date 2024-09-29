package com.pocketbud.pocketbud.budget;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    // You can add custom query methods if needed
}
