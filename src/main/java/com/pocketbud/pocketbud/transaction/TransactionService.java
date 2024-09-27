package com.pocketbud.pocketbud.transaction;

import com.pocketbud.pocketbud.category.Category;
import com.pocketbud.pocketbud.category.CategoryGroup;
import com.pocketbud.pocketbud.category.CategoryGroupRepository;
import com.pocketbud.pocketbud.model.User;
import com.pocketbud.pocketbud.category.CategoryRepository;
import com.pocketbud.pocketbud.repository.UserRepository;
import com.pocketbud.pocketbud.tag.Tag;
import com.pocketbud.pocketbud.tag.TagRepository;
import com.pocketbud.pocketbud.transaction.dto.TransactionRequestDTO;
import com.pocketbud.pocketbud.transaction.dto.TransactionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryGroupRepository categoryGroupRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    // Retrieve all transactions
    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Create a new transaction
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionRequestDTO requestDTO) {

        // Fetch the user entity
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        // Fetch the category entity
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));

        Set<Tag> tags = new HashSet<>();
        if (requestDTO.getTagIds() != null && !requestDTO.getTagIds().isEmpty()) {
            tagCreator(requestDTO, tags);
        }

        Transaction transaction = Transaction.builder()
                .amount(requestDTO.getAmount())
                .date(requestDTO.getDate())
                .user(user) // Assume this is handled
                .category(category) // Assume this is handled
                .type(requestDTO.getType())
                .isRepeated(requestDTO.getIsRepeated())
                .isIrregular(requestDTO.getIsIrregular())
                .description(requestDTO.getDescription())  // Set description
                .tags(tags)
                .recurrenceInterval(requestDTO.getRecurrenceInterval())  // Set recurrence interval
                .recurrenceUnit(requestDTO.getRecurrenceUnit())  // Set recurrence unit
                .build();

        transactionRepository.save(transaction);

        // If this is an EXPENSE transaction, reduce category allowance
        if (transaction.getType() == TransactionType.EXPENSE) {
            updateCategoryAllowance(transaction);
        }

        // If it's a recurring transaction, create future occurrences
        if (transaction.getIsRepeated()) {
            createRecurringTransactions(transaction);
        }

        return mapToDTO(transaction);
    }

    // Update an existing transaction
    @Transactional
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO requestDTO) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Store original repeated state
        Boolean wasRepeated = transaction.getIsRepeated();

        transaction.setAmount(requestDTO.getAmount());
        transaction.setDate(requestDTO.getDate());
        transaction.setType(requestDTO.getType());
        transaction.setDescription(requestDTO.getDescription());
        transaction.setIsRepeated(requestDTO.getIsRepeated());
        transaction.setIsIrregular(requestDTO.getIsIrregular());

        // Fetch and set category
        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        transaction.setCategory(category);

        // Fetch and set user
        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        transaction.setUser(user);

        // Update recurrence details
        transaction.setRecurrenceInterval(requestDTO.getRecurrenceInterval());
        transaction.setRecurrenceUnit(requestDTO.getRecurrenceUnit());

        // Handle tags
        if (requestDTO.getTagIds() != null && !requestDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>();
            tagCreator(requestDTO, tags);
            transaction.setTags(tags);  // Set updated tags
        }

        // Handle changes in recurring transactions
        if (requestDTO.getIsRepeated() != null && requestDTO.getIsRepeated() != wasRepeated) {
            if (requestDTO.getIsRepeated()) {
                // Create new future occurrences based on updated details
                createRecurringTransactions(transaction);
            } else {
                // Delete future recurring transactions if it is no longer marked as repeated
                deleteFutureRecurringTransactions(transaction);
            }
        }

        transactionRepository.save(transaction);

        // If this is an EXPENSE transaction, reduce category allowance
        if (transaction.getType() == TransactionType.EXPENSE) {
            updateCategoryAllowance(transaction);
        }

        return mapToDTO(transaction);
    }

    private void tagCreator(TransactionRequestDTO requestDTO, Set<Tag> tags) {
        for (Long tagId : requestDTO.getTagIds()) {
            Tag tag = tagRepository.findById(tagId)
                    .orElseGet(() -> {
                        // Create a new tag if it doesn't exist
                        Tag newTag = new Tag();
                        newTag.setId(tagId);  // Assuming tagId is set, or else use some other unique identifier.
                        newTag.setName("Default Tag Name");  // Set a default name or get from requestDTO if available
                        return tagRepository.save(newTag);  // Save and return the new tag
                    });
            tags.add(tag);
        }
    }

    // Delete a transaction
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    // Retrieve a specific transaction by ID
    public TransactionResponseDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToDTO(transaction);
    }


    // Map transaction entity to response DTO
    private TransactionResponseDTO mapToDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setType(TransactionType.valueOf(transaction.getType().name()));
        dto.setDescription(transaction.getDescription());
        dto.setCategoryName(transaction.getCategory().getName());
        dto.setUserId(transaction.getUser().getId());
        dto.setIsRepeated(transaction.getIsRepeated());
        dto.setIsIrregular(transaction.getIsIrregular());
        dto.setTags(transaction.getTags());
        return dto;
    }

    private void createRecurringTransactions(Transaction transaction) {
        if (transaction.getRecurrenceInterval() != null && transaction.getRecurrenceUnit() != null) {
            LocalDate nextDate = transaction.getDate();

            // Example: create the next 5 occurrences
            for (int i = 0; i < 5; i++) {
                nextDate = nextDate.plus(transaction.getRecurrenceInterval(), transaction.getRecurrenceUnit());
                Transaction newTransaction = transaction.toBuilder()
                        .id(null)  // Set to null to create a new entry
                        .date(nextDate)
                        .build();

                transactionRepository.save(newTransaction);
            }
        }
    }

    private void deleteFutureRecurringTransactions(Transaction transaction) {
        LocalDate currentDate = LocalDate.now();

        // Find all future transactions based on the current transaction ID and user ID
        List<Transaction> futureTransactions = transactionRepository.findFutureTransactions(transaction.getId(), transaction.getUser().getId(), currentDate);

        // Delete the found transactions
        if (!futureTransactions.isEmpty()) {
            transactionRepository.deleteAll(futureTransactions);
        }
    }

    private void updateCategoryAllowance(Transaction transaction) {
        Category category = transaction.getCategory();

        // Reduce the category's current allowance by the transaction amount
        double newAllowance = category.getCurrentAllowance() - transaction.getAmount();
        category.setCurrentAllowance(newAllowance);

        // Save the updated category
        categoryRepository.save(category);

        if(categoryGroupRepository.findById(category.getCategoryGroupId()).isPresent())
            // Recalculate the CategoryGroup's current allowance
            updateCategoryGroupAllowance(categoryGroupRepository.findById(category.getCategoryGroupId()).get());
    }

    private void updateCategoryGroupAllowance(CategoryGroup categoryGroup) {
        // Calculate the new total allowance for the category group
        double totalAllowance = categoryGroup.getCategories()
                .stream()
                .mapToDouble(Category::getCurrentAllowance)
                .sum();

        categoryGroup.setCurrentGroupAllowance(totalAllowance);

        // Save the updated category group
        categoryGroupRepository.save(categoryGroup);
    }

}
