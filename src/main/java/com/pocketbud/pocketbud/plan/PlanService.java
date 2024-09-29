package com.pocketbud.pocketbud.plan;

import com.pocketbud.pocketbud.allocation.Allocation;
import com.pocketbud.pocketbud.budget.Budget;
import com.pocketbud.pocketbud.budget.BudgetRepository;
import com.pocketbud.pocketbud.plan.dto.PlanRequestDTO;
import com.pocketbud.pocketbud.plan.dto.PlanResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final BudgetRepository budgetRepository;

    public PlanResponseDTO createPlan(PlanRequestDTO requestDTO) {

        Budget budget = budgetRepository.findById(requestDTO.getBudget_id())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user budgetId"));

        boolean completed = false;

        Plan plan = Plan.builder()
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .budget(budget)
                .completed(completed)
                .fragmentation(requestDTO.getFragments())
                .build();

        Set<Allocation> allocations = requestDTO.getAllocations();

        if (allocations != null && !allocations.isEmpty()) {
            divide_fragments(
                    requestDTO.getFragments(),
                    requestDTO.getAllocation_type(),
                    requestDTO.getAllocations(),
                    budget
            );
            completed = true;
            plan.setAllocations(allocations);
            plan.setCompleted(completed);
        }

        planRepository.save(plan);

        savePlanToBudget(budget, plan);

        return mapToDTO(plan);
    }

    private PlanResponseDTO mapToDTO(Plan plan) {
        PlanResponseDTO dto = new PlanResponseDTO();
        return dto;
    }

//
    private void divide_fragments(
            Set<Double> fragments,
            AllocationType allocationType,
            Set<Allocation> allocations,
            Budget budget
    ) {
        if (allocationType == AllocationType.PERCENTAGE) {
            if (fragments.isEmpty()) return;

            double totalPercentage = fragments.stream().mapToDouble(Double::doubleValue).sum();
            if (totalPercentage > 100) {
                throw new IllegalArgumentException("Total percentage cannot exceed 100%");
            }

            int i = 0;
            for (Allocation allocation : allocations) {
                Double fragment = (Double) fragments.toArray()[i];
                Double allocatedAmount = budget.getTotalAmount() * (fragment / 100);
                allocation.setAllocatedAmount(allocatedAmount);
                allocation.applyAllocation(); // Apply allocation to the categories
                i++;
            }
        } else {
             if (fragments.isEmpty()) return;

            double totalAmount = fragments.stream().mapToDouble(Double::doubleValue).sum();
            if (totalAmount > budget.getTotalAmount()) {
                throw new IllegalArgumentException("Total amount cannot exceed budget");
            }

            int i = 0;
            for (Allocation allocation : allocations) {
                Double fixedAmount = (Double) fragments.toArray()[i];
                allocation.setAllocatedAmount(fixedAmount);
                allocation.applyAllocation(); // Apply allocation to the categories
                i++;
            }
        }
    }

    private void savePlanToBudget(Budget budget, Plan plan) {
        if (budget.getPlans().isEmpty()) {
            budget.setPlans(Set.of(plan));
        } else {
            Set<Plan> existingPlans = budget.getPlans();
            existingPlans.add(plan);
        }
    }


}
