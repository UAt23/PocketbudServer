package com.pocketbud.pocketbud.category.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryGroupResponseDTO {
    private Long id;
    private String name;
    private Double groupAllowance;
    private List<CategoryResponseDTO> categories;  // Include associated categories
}
