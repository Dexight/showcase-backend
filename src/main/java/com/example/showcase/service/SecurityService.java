package com.example.showcase.service;

import com.example.showcase.entity.Project;
import com.example.showcase.entity.User;
import com.example.showcase.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userService;
    private final ProjectService projectService;
    public boolean canEditProject(Authentication authentication, int projectId) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email").toString();
        User user = userService.findByEmail(email);
        if (isAdmin(user)) {
            return true;
        }
        Project project = projectService.getProjectById(projectId);
        return project.getUsers()
                    .stream()
                    .anyMatch(u -> u.getId() == user.getId());
    }

    public boolean hasAdminAccess (Authentication authentication) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email").toString();
        User user = userService.findByEmail(email);
        return isAdmin(user);
    }

    public boolean hasSuperAdminAccess (Authentication authentication) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email").toString();
        User user = userService.findByEmail(email);
        return isSuperAdmin(user);
    }


    private boolean isAdmin(User user) {
        int roleId = user.getRole().getId();
        return (roleId == 2) || (roleId == 4);
    }

    private boolean isSuperAdmin(User user) {
        int roleId = user.getRole().getId();
        return (roleId == 4);
    }
}
