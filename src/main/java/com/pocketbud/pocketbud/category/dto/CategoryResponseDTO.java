package com.pocketbud.pocketbud.category.dto;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Double allowance;
    private String type;  // INCOME or EXPENSE
    private String icon;
    private String color;
}