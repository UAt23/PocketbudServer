package com.pocketbud.pocketbud.repository;


import com.pocketbud.pocketbud.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
