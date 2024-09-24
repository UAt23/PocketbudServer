package com.pocketbud.pocketbud.category;

import com.pocketbud.pocketbud.category.dto.CategoryGroupRequestDTO;
import com.pocketbud.pocketbud.category.dto.CategoryGroupResponseDTO;
import com.pocketbud.pocketbud.category.dto.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;

    // Create or update category group
    public CategoryGroupResponseDTO createOrUpdateCategoryGroup(CategoryGroupRequestDTO requestDTO) {
        CategoryGroup group = new CategoryGroup();
        group.setName(requestDTO.getName());
        group.setGroupAllowance(requestDTO.getGroupAllowance());

        group = categoryGroupRepository.save(group);
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

        // Map categories if needed
        dto.setCategories(
                group.getCategories().stream()
                        .map(category -> {
                            CategoryResponseDTO categoryDTO = new CategoryResponseDTO();
                            categoryDTO.setId(category.getId());
                            categoryDTO.setName(category.getName());
                            categoryDTO.setAllowance(category.getAllowance());
                            categoryDTO.setType(category.getType());
                            categoryDTO.setIcon(category.getIcon());
                            categoryDTO.setColor(category.getColor());
                            return categoryDTO;
                        })
                        .collect(Collectors.toList())
        );
        return dto;
    }
}
