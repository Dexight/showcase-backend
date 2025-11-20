package com.example.showcase.controllers;

import com.example.showcase.dto.ProjectUserDTO;
import com.example.showcase.entity.ProjectUser;
import com.example.showcase.service.ProjectUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/project_users")
public class ProjectUserController {
    private ProjectUserService projectUserService;

    @PostMapping
    public ResponseEntity<ProjectUser> createProjectUser(@RequestBody ProjectUserDTO projectUserDTO) {
        ProjectUser savedProjectUser = projectUserService.createProjectUser(projectUserDTO);
        return new ResponseEntity<>(savedProjectUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectUser> getProjectUserById(@PathVariable("id") int projectUserId) {
        ProjectUser projectUser = projectUserService.getProjectUserById(projectUserId);
        return ResponseEntity.ok(projectUser);
    }

    @GetMapping
    public ResponseEntity<List<ProjectUser>> getAllProjectUsers() {
        List<ProjectUser> projectUsers = projectUserService.getAllProjectUsers();
        return ResponseEntity.ok(projectUsers);
    }

    @PutMapping("{id}")
    public ResponseEntity<ProjectUser> updateProjectUser(@PathVariable("id") int projectUserId, @RequestBody ProjectUserDTO updateProjectUserDTO) {
        ProjectUser projectUser = projectUserService.updateProjectUser(projectUserId, updateProjectUserDTO);
        return ResponseEntity.ok(projectUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProjectUser(@PathVariable("id") int projectUserId) {
        projectUserService.deleteProjectUser(projectUserId);
        return ResponseEntity.ok("ProjectUser is deleted");
    }
}
