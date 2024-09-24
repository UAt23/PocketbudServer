package com.pocketbud.pocketbud.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("SELECT t FROM Transaction t WHERE t.id != :transactionId AND t.user.id = :userId AND t.date > :currentDate")
    List<Transaction> findFutureTransactions(@Param("transactionId") Long transactionId, @Param("userId") Integer userId, @Param("currentDate") LocalDate currentDate);
}
