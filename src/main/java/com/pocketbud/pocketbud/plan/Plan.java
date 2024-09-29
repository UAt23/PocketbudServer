package com.pocketbud.pocketbud.plan;

import com.pocketbud.pocketbud.allocation.Allocation;
import com.pocketbud.pocketbud.budget.Budget;
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
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "completed")
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "budget_id", referencedColumnName = "id", nullable = false)
    private Budget budget; // Associated budget with this plan

    @ElementCollection
    @CollectionTable(name = "plan_fragmentation", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "fragmentation", nullable = false)
    private Set<Double> fragmentation = new HashSet<>(); // Percentages for allocations

    @Column(name = "allocation_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AllocationType allocationType; // Allocation type (PERCENTAGE or AMOUNT)

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Allocation> allocations = new HashSet<>(); // Allocations belonging to this plan
}
