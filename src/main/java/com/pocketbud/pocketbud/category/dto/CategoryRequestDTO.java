package com.pocketbud.pocketbud.category.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryRequestDTO {
    private String name;
    private Double allowance;
    private String type;  // INCOME or EXPENSE
    private String icon;
    private String color;
    private List<Integer> categoryGroupIds;
}
