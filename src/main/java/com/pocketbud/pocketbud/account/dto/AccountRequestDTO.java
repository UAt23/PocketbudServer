package com.pocketbud.pocketbud.account.dto;

import lombok.Data;

@Data
public class AccountRequestDTO {
    private String name;
    private Double balance;
    private Integer userId;
    private String description;
    private String type;
}