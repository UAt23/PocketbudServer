package com.pocketbud.pocketbud.category;

import com.pocketbud.pocketbud.transaction.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double allowance;  // Amount allocated for this category

    @Column(nullable = false)
    private Double currentAllowance = 0.0;  // The remaining allowance after expenses

    @Column(nullable = false)
    private String type;  // "INCOME" or "EXPENSE"

    @Column(nullable = true)
    private String icon;  // Optional category icon

    @Column(nullable = true, name = "color")
    private String color; // Optional category color

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = true, name = "transactions")
    private Set<Transaction> transactions;

    @JoinColumn(name = "category_group_id")
    private Long categoryGroupId;  // Add relationship to CategoryGroup


}
