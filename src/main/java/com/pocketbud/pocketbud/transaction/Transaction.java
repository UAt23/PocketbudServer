package com.pocketbud.pocketbud.transaction;

import com.pocketbud.pocketbud.account.Account;
import com.pocketbud.pocketbud.category.Category;
import com.pocketbud.pocketbud.tag.Tag;
import com.pocketbud.pocketbud.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;  // INCOME or EXPENSE

    @Column(length = 255, nullable = true)  // Adding description with a reasonable length
    private String description;

    private Boolean isRepeated;  // Flag for repeated transactions

    private Boolean isIrregular; // Flag for irregular transactions

    @ManyToMany
    @JoinTable(
            name = "transaction_tags",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Column(nullable = true)
    private Integer recurrenceInterval;  // How often the transaction repeats (e.g., every 1 day, 1 week)

    @Enumerated(EnumType.STRING)
    private ChronoUnit recurrenceUnit;  // Time unit for recurrence (days, weeks, months, etc.)

    public void setTags(Set<Tag> tags) {
        this.tags = new HashSet<>(tags);  // Defensive copy to avoid shared references
    }
}
