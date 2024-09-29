package com.pocketbud.pocketbud.budget;

import com.pocketbud.pocketbud.account.Account;
import com.pocketbud.pocketbud.plan.Plan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = true)
    private Double availableAmount;

    @OneToMany(mappedBy = "budget")
    private Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "budget")
    private Set<Plan> plans = new HashSet<>();

    // Add any additional fields you might need
}