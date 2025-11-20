package com.example.showcase.controllers;

import com.example.showcase.dto.ProjectTagDTO;
import com.example.showcase.entity.ProjectTag;
import com.example.showcase.service.ProjectTagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/project_tags")
public class ProjectTagController {
    private ProjectTagService projectTagService;

    @PostMapping
    public ResponseEntity<ProjectTag> createProjectTag(@RequestBody ProjectTagDTO projectTagDTO) {
        ProjectTag savedProjectTag = projectTagService.createProjectTag(projectTagDTO);
        return new ResponseEntity<>(savedProjectTag, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectTag> getProjectTagById(@PathVariable("id") int projectTagId) {
        ProjectTag projectTag = projectTagService.getProjectTagById(projectTagId);
        return ResponseEntity.ok(projectTag);
    }

    @GetMapping
    public ResponseEntity<List<ProjectTag>> getAllProjectTags() {
        List<ProjectTag> projectTags = projectTagService.getAllProjectTags();
        return ResponseEntity.ok(projectTags);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProjectTag> updateProjectTag(@PathVariable("id") int projectTagId, @RequestBody ProjectTagDTO updateProjectTagDTO) {
        ProjectTag projectTag = projectTagService.updateProjectTag(projectTagId, updateProjectTagDTO);
        return ResponseEntity.ok(projectTag);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProjectTag(@PathVariable("id") int projectTagId) {
       projectTagService.deleteProjectTag(projectTagId);
       return ResponseEntity.ok("ProjectTag is deleted");
    }
}
