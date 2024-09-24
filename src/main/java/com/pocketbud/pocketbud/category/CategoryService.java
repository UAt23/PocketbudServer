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

    // Create or update a category
    public CategoryResponseDTO createOrUpdateCategory(CategoryRequestDTO requestDTO) {
        Category category = new Category();
        category.setName(requestDTO.getName());
        category.setAllowance(requestDTO.getAllowance());
        category.setType(requestDTO.getType());
        category.setIcon(requestDTO.getIcon());
        category.setColor(requestDTO.getColor());

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
        dto.setType(category.getType());
        dto.setIcon(category.getIcon());
        dto.setColor(category.getColor());
        return dto;
    }
}