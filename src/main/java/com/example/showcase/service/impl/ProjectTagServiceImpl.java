package com.example.showcase.service.impl;

import com.example.showcase.dto.ProjectTagDTO;
import com.example.showcase.entity.Project;
import com.example.showcase.entity.Tag;
import com.example.showcase.exception.ResourceNotFoundException;
import com.example.showcase.entity.ProjectTag;
import com.example.showcase.repository.ProjectRepository;
import com.example.showcase.repository.ProjectTagRepository;
import com.example.showcase.repository.TagRepository;
import com.example.showcase.service.ProjectTagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectTagServiceImpl implements ProjectTagService {

    private ProjectTagRepository projectTagRepository;
    private TagRepository tagRepository;
    private ProjectRepository projectRepository;
    
    @Override
    public ProjectTag createProjectTag(ProjectTagDTO projectTagDTO) {
        Optional<Project> project = projectRepository.findById(projectTagDTO.getProjectId());
        Optional<Tag> tag = tagRepository.findById(projectTagDTO.getTagId());
        if (project.isPresent() && tag.isPresent()) {
            ProjectTag projectTag = new ProjectTag();
            projectTag.setProject(project.get());
            projectTag.setTag(tag.get());
            return projectTagRepository.save(projectTag);
        }
        else
            throw new ResourceNotFoundException("Project or tag not found!");
    }

    @Override
    public ProjectTag getProjectTagById(int projectTagId) {
        ProjectTag projectTag = projectTagRepository.findById(projectTagId)
            .orElseThrow(() -> new ResourceNotFoundException("ProjectTag not found"));
        return projectTag;
    }

    @Override
    public List<ProjectTag> getAllProjectTags() {
        return projectTagRepository.findAll();
    }

    @Override
    public ProjectTag updateProjectTag(int projectTagId, ProjectTagDTO updateProjectTagDTO) {
        ProjectTag projectTag = projectTagRepository.findById(projectTagId)
                .orElseThrow(() -> new ResourceNotFoundException("ProjectTag not found"));

        Project project = projectRepository.findById(updateProjectTagDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Tag tag = tagRepository.findById(updateProjectTagDTO.getTagId())
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found"));

        projectTag.setProject(project);
        projectTag.setTag(tag);

        return projectTagRepository.save(projectTag);
    }

    @Override
    public void deleteProjectTag(int projectTagId) {
        ProjectTag projectTag = projectTagRepository.findById(projectTagId)
            .orElseThrow(() -> new ResourceNotFoundException("ProjectTag not found"));
        projectTagRepository.delete(projectTag);
    }
}
