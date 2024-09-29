package com.pocketbud.pocketbud.allocation;

import com.pocketbud.pocketbud.category.Category;
import com.pocketbud.pocketbud.plan.Plan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "allocations")
public class Allocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan; // Reference to the plan

    @ManyToMany // Many allocations can involve many categories
    @JoinTable(
            name = "allocation_categories",
            joinColumns = @JoinColumn(name = "allocation_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>(); // Categories affected by this allocation

    private Double allocatedAmount; // Total amount allocated for this allocation

    @ElementCollection
    @CollectionTable(name = "allocation_amounts", joinColumns = @JoinColumn(name = "allocation_id"))
    @MapKeyJoinColumn(name = "category_id") // Reference to the category ID
    @Column(name = "allocated_amount") // Amount allocated to the respective category
    private Map<Long, Double> amounts = new HashMap<>(); // Mapping of category ID to allocated amount

    // Method to apply allocated amounts to categorize
    public void applyAllocation() {
        for (Map.Entry<Long, Double> entry : amounts.entrySet()) {
            Long categoryId = entry.getKey();
            Double allocatedAmountForCategory = entry.getValue();

            // Find the category by its ID
            categories.stream()
                    .filter(category -> category.getId().equals(categoryId))
                    .findFirst()
                    .ifPresent(category -> {
                        double newAllowance = category.getCurrentAllowance() - allocatedAmountForCategory;
                        category.setCurrentAllowance(Math.max(newAllowance, 0)); // Update category allowance
                    });
        }
    }

    // Method to reset allowances if needed
    public void resetAllocations() {
        for (Category category : categories) {
            category.setCurrentAllowance(category.getAllowance()); // Reset to original allowance
        }
    }
}
