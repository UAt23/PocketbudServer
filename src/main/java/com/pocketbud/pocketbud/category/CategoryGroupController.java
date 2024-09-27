package com.pocketbud.pocketbud.category;

import com.pocketbud.pocketbud.category.dto.CategoryGroupRequestDTO;
import com.pocketbud.pocketbud.category.dto.CategoryGroupResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/category-groups")
@RequiredArgsConstructor
public class CategoryGroupController {

    private final CategoryGroupService categoryGroupService;

    // Get all category groups
    @GetMapping
    public ResponseEntity<List<CategoryGroupResponseDTO>> getAllCategoryGroups() {
        List<CategoryGroupResponseDTO> groups = categoryGroupService.getAllCategoryGroups();
        return ResponseEntity.ok(groups);
    }

    // Get category group by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryGroupResponseDTO> getCategoryGroupById(@PathVariable Long id) {
        CategoryGroupResponseDTO group = categoryGroupService.getCategoryGroupById(id);
        return ResponseEntity.ok(group);
    }

    // Create category group
    @PostMapping
    public ResponseEntity<CategoryGroupResponseDTO> createCategoryGroup(@RequestBody CategoryGroupRequestDTO requestDTO) {
        CategoryGroupResponseDTO createdGroup = categoryGroupService.createCategoryGroup(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    // Delete category group by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryGroup(@PathVariable Long id) {
        categoryGroupService.deleteCategoryGroup(id);
        return ResponseEntity.noContent().build();
    }
}
