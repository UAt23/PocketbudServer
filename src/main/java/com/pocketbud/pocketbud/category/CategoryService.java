package com.pocketbud.pocketbud.category;

import com.pocketbud.pocketbud.category.dto.CategoryRequestDTO;
import com.pocketbud.pocketbud.category.dto.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryGroupRepository categoryGroupRepository;


    // Create or update a category
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setName(requestDTO.getName());
        category.setAllowance(requestDTO.getAllowance());
        category.setCurrentAllowance(requestDTO.getAllowance());
        category.setType(requestDTO.getType());
        category.setIcon(requestDTO.getIcon());
        category.setColor(requestDTO.getColor());


        if(categoryGroupRepository.findById(requestDTO.getCategoryGroupId()).isPresent()) {
            category.setCategoryGroupId(requestDTO.getCategoryGroupId());
            // TODO GROUP ALLOWANCE UPDATE
            CategoryGroup belongingGroup = categoryGroupRepository.findById(requestDTO.getCategoryGroupId()).get();
            belongingGroup.getCategories().add(category);
            System.out.println(belongingGroup);
            belongingGroup.setCurrentGroupAllowance(requestDTO.getAllowance());
            belongingGroup.setGroupAllowance();
        }

        category = categoryRepository.save(category);



        return mapToDTO(category);
    }

    // Get all categories
    public List<CategoryResponseDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get a category by ID
    public CategoryResponseDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapToDTO(category);
    }

    // Delete a category
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    // Helper: Map Category entity to CategoryResponseDTO
    private CategoryResponseDTO mapToDTO(Category category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setAllowance(category.getAllowance());
        dto.setCurrentAllowance(category.getCurrentAllowance());
        dto.setType(category.getType());
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        dto.setCategoryGroupId(category.getCategoryGroupId());
        return dto;
    }
}