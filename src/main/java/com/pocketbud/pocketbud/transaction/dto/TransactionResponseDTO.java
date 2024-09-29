package com.pocketbud.pocketbud.transaction.dto;

import com.pocketbud.pocketbud.tag.Tag;
import com.pocketbud.pocketbud.transaction.TransactionType;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class TransactionResponseDTO {
    private Long id;
    private Double amount;
    private LocalDate date;
    private TransactionType type;        // "INCOME" or "EXPENSE"
    private String description;
    private String categoryName;
    private Integer userId;        // ID of the user who performed the transaction
    private Long accountId;  // Assuming you're passing category ID
    private Boolean isRepeated;
    private Boolean isIrregular;
    private Set<Tag> tags;
}
