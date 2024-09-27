package com.pocketbud.pocketbud.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_groups")
public class CategoryGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false, name = "group_allowance")
    private Double groupAllowance = 0.0;  // Total allowance for this category group

    @Column(nullable = false)
    private Double currentGroupAllowance = 0.0;  // The remaining allowance of all categories combined

    @OneToMany(mappedBy = "categoryGroupId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Category> categories;

    public void setGroupAllowance() {
        if (categories != null && !categories.isEmpty())
            this.groupAllowance = categories.stream().mapToDouble(Category::getAllowance).sum();
        else
            this.groupAllowance = 0.0;
    }

    public void setCurrentGroupAllowance(Double currentGroupAllowance) {
        this.currentGroupAllowance =  this.groupAllowance + currentGroupAllowance;
    }
}
