package com.pocketbud.pocketbud.budget.dto;

import com.pocketbud.pocketbud.account.Account;
import lombok.Data;

import java.util.Set;

@Data
public class BudgetResponseDTO {
    private Long id;
    private Double totalAmount;
    private Double availableAmount;
    private Set<Account> accounts;
}