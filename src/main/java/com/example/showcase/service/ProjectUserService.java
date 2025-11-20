package com.example.showcase.service;

import com.example.showcase.dto.ProjectUserDTO;
import com.example.showcase.entity.ProjectUser;

import java.util.List;

public interface ProjectUserService {
    ProjectUser createProjectUser(ProjectUserDTO projectUserDTO);

    ProjectUser getProjectUserById(int userId);

    List<ProjectUser> getAllProjectUsers();

    ProjectUser updateProjectUser(int userId, ProjectUserDTO updateProjectUserDTO);

    void deleteProjectUser(int userId);
}
