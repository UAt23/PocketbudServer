package com.pocketbud.pocketbud.tag;

import com.pocketbud.pocketbud.tag.dto.TagRequestDTO;
import com.pocketbud.pocketbud.tag.dto.TagResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagRepository tagRepository;

    @GetMapping
    public List<TagResponseDTO> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return tags.stream().map(this::mapToDTO).toList();
    }

    @PostMapping
    public TagResponseDTO createTag(@RequestBody TagRequestDTO requestDTO) {
        Tag tag = new Tag();
        tag.setName(requestDTO.getName());
        Tag savedTag = tagRepository.save(tag);
        return mapToDTO(savedTag);
    }

    private TagResponseDTO mapToDTO(Tag tag) {
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
