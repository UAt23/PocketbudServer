package com.pocketbud.pocketbud.budget;

import com.pocketbud.pocketbud.account.Account;
import com.pocketbud.pocketbud.budget.dto.BudgetRequestDTO;
import com.pocketbud.pocketbud.budget.dto.BudgetResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    public BudgetResponseDTO createBudget(BudgetRequestDTO requestDTO) {
        Set<Account> accounts = requestDTO.getAccounts();
        Double total =  calculateTotalAmount(accounts);
        Budget budget = Budget.builder()
                .totalAmount(total)
                .availableAmount(total)
                .accounts(accounts)
                .build();

        budgetRepository.save(budget);

        return mapToDTO(budget);
    }

    public Budget updateBudget(Long id, Budget budgetDetails) {
        Budget budget = getBudgetById(id);
        budget.setTotalAmount(budgetDetails.getTotalAmount());
        budget.setAvailableAmount(budgetDetails.getAvailableAmount());
        // Update other fields as needed
        return budgetRepository.save(budget);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    private BudgetResponseDTO mapToDTO(Budget budget) {
        BudgetResponseDTO dto = new BudgetResponseDTO();
        dto.setId(budget.getId());
        dto.setTotalAmount(budget.getTotalAmount());
        dto.setAccounts(budget.getAccounts());

        return dto;
    }

    private double calculateTotalAmount(Set<Account> accounts) {
       return accounts.stream().mapToDouble(Account::getBalance).sum();
    }


}
