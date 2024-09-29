package com.pocketbud.pocketbud.account.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountResponseDTO {
    private Long id;
    private String name;
    private Double balance;
    private String description;
    private String type;
    private Integer userId;
}
