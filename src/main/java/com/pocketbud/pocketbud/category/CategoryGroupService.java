package com.pocketbud.pocketbud.category;

import com.pocketbud.pocketbud.category.dto.CategoryGroupRequestDTO;
import com.pocketbud.pocketbud.category.dto.CategoryGroupResponseDTO;
import com.pocketbud.pocketbud.category.dto.CategoryRequestDTO;
import com.pocketbud.pocketbud.category.dto.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;
    private final CategoryRepository categoryRepository;

    // Create or update category group
    public CategoryGroupResponseDTO createCategoryGroup(CategoryGroupRequestDTO requestDTO) {


        CategoryGroup group = new CategoryGroup();
        group.setName(requestDTO.getName());
        group.setDescription(requestDTO.getDescription());


        // Save the CategoryGroup first to generate an ID
        CategoryGroup savedGroup = categoryGroupRepository.save(group);

        // Fetch categories using the provided category IDs
//        List<Category> categories = categoryRepository.findAllById(requestDTO.getCategories());
//        group.setCategories(categories);
//
//        // Set the categoryGroupId for each category without setting the whole CategoryGroup object
//        categories.forEach(category -> category.setCategoryGroupId(savedGroup.getId()));
//
//        // Save the updated categories
//        categoryRepository.saveAll(categories);

        return mapToDTO(group);
    }

    // Get all category groups
    public List<CategoryGroupResponseDTO> getAllCategoryGroups() {
        return categoryGroupRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get category group by ID
    public CategoryGroupResponseDTO getCategoryGroupById(Long id) {
        CategoryGroup group = categoryGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category Group not found"));

        return mapToDTO(group);
    }

    // Delete category group
    public void deleteCategoryGroup(Long id) {
        categoryGroupRepository.deleteById(id);
    }

    // Helper: Map CategoryGroup entity to CategoryGroupResponseDTO
    private CategoryGroupResponseDTO mapToDTO(CategoryGroup group) {
        CategoryGroupResponseDTO dto = new CategoryGroupResponseDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setGroupAllowance(group.getGroupAllowance());
        dto.setCurrentGroupAllowance(group.getCurrentGroupAllowance());
        if (group.getCategories() != null && !group.getCategories().isEmpty()) {
            // Map categories without fetching the whole CategoryGroup object
            dto.setCategories(
                    group.getCategories().stream()
                            .map(category -> {
                                CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
                                categoryDTO.setId(category.getId());
                                categoryDTO.setName(category.getName());
                                categoryDTO.setAllowance(category.getAllowance());
                                categoryDTO.setCurrentAllowance(category.getCurrentAllowance());
                                categoryDTO.setType(category.getType());
                                categoryDTO.setIcon(category.getIcon());
                                categoryDTO.setColor(category.getColor());
                                categoryDTO.setCategoryGroupId(category.getCategoryGroupId());  // Set the group ID directly
                                return categoryDTO;
                            })
                            .collect(Collectors.toList())
            );
        } else {
            dto.setCategories(Collections.emptyList()); // Handle empty or null categories
        }
        return dto;
    }
}
