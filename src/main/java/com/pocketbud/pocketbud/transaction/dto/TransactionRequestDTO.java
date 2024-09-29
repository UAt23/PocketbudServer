package com.pocketbud.pocketbud.transaction.dto;

import com.pocketbud.pocketbud.transaction.TransactionType;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Data
public class TransactionRequestDTO {
    private Double amount;
    private LocalDate date;
    private Integer userId;  // Assuming you're passing user ID
    private Long categoryId;  // Assuming you're passing category ID
    private Long accountId;  // Assuming you're passing category ID
    private TransactionType type;
    private Boolean isRepeated;
    private Boolean isIrregular;
    private String description;  // New field
    private List<Long> tagIds;
    private Integer recurrenceInterval;  // New field for recurrence interval
    private ChronoUnit recurrenceUnit;   // New field for recurrence unit
}