package com.example.showcase.service.impl;

import com.example.showcase.dto.ProjectUserDTO;
import com.example.showcase.entity.*;
import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.repository.*;
import com.example.showcase.service.ProjectUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectUserServiceImpl implements ProjectUserService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectUserRepository projectUserRepository;

    @Override
    public ProjectUser createProjectUser(ProjectUserDTO projectUserDTO) {
        Optional<Project> project = projectRepository.findById(projectUserDTO.getProjectId());
        Optional<User> user = userRepository.findById(projectUserDTO.getUserId());
        if (project.isPresent() && user.isPresent()) {
            ProjectUser projectUser = new ProjectUser();
            projectUser.setProject(project.get());
            projectUser.setUser(user.get());
            return projectUserRepository.save(projectUser);
        }
        else
            throw new ResourceNotFoundException("Project or tag not found!");
    }

    @Override
    public ProjectUser getProjectUserById(int projectUserId) {
        ProjectUser projectUser = projectUserRepository.findById(projectUserId)
            .orElseThrow(() -> new ResourceNotFoundException("ProjectUser not found"));
        return projectUser;
    }

    @Override
    public List<ProjectUser> getAllProjectUsers() {
        return projectUserRepository.findAll();
    }

    @Override
    public ProjectUser updateProjectUser(int projectUserId, ProjectUserDTO updateProjectUserDTO) {
        ProjectUser projectUser = projectUserRepository.findById(projectUserId)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectTag not found"));

        Project project = projectRepository.findById(updateProjectUserDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User user = userRepository.findById(updateProjectUserDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        projectUser.setProject(project);
        projectUser.setUser(user);

        return projectUserRepository.save(projectUser);
    }

    @Override
    public void deleteProjectUser(int projectTagId) {
        ProjectUser projectTag = projectUserRepository.findById(projectTagId)
            .orElseThrow(() -> new ResourceNotFoundException("ProjectUser not found"));
        projectUserRepository.delete(projectTag);
    }
}
