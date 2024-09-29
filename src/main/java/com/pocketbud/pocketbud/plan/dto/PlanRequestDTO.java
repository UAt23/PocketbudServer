package com.pocketbud.pocketbud.plan.dto;

import com.pocketbud.pocketbud.allocation.Allocation;
import com.pocketbud.pocketbud.plan.AllocationType;
import lombok.Data;

import java.util.Set;

@Data
public class PlanRequestDTO {
    private String name;
    private String description;
    private Long budget_id;
    private AllocationType allocation_type;
    private Set<Double> fragments;
    private Set<Allocation> allocations;
}
