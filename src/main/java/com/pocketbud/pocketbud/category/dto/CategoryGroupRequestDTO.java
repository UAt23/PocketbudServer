package com.pocketbud.pocketbud.category.dto;

import com.pocketbud.pocketbud.category.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryGroupRequestDTO {
    private String name;
    private Double groupAllowance;
    private String description;
    private List<Long> categories;
}
