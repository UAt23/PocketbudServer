package com.pocketbud.pocketbud.account;

import com.pocketbud.pocketbud.budget.Budget;
import com.pocketbud.pocketbud.model.User;
import com.pocketbud.pocketbud.transaction.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double balance = 0.0;  // Current balance of the account

    @Column(nullable = false)
    private Double currentBudget;  // Remaining budget in the account after transactions

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id", nullable = false)
    private Budget budget;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    // Optional fields: description, type, etc.
    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String type;

    public void increaseBalance(Double amount) {
        this.balance += amount;
    }

    public void decreaseBalance(Double amount) {
        this.balance -= amount;
    }

}
