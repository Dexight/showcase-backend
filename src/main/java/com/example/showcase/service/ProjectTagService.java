package com.example.showcase.service;

import com.example.showcase.dto.ProjectTagDTO;
import com.example.showcase.entity.ProjectTag;

import java.util.List;

public interface ProjectTagService {
    ProjectTag createProjectTag(ProjectTagDTO projectTagDTO);

    ProjectTag getProjectTagById(int projectId);

    List<ProjectTag> getAllProjectTags();

    ProjectTag updateProjectTag(int projectId, ProjectTagDTO updateProjectTagDTO);

    void deleteProjectTag(int projectId);
}
