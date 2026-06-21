package com.example.showcase.controllers;

import com.example.showcase.dto.ProjectDTO;
import com.example.showcase.entity.Date;
import com.example.showcase.entity.Project;
import com.example.showcase.entity.Tag;
import com.example.showcase.entity.Track;
import com.example.showcase.primary_filling.PrimaryFillingLoader;
import com.example.showcase.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "https://showcase-pd.ru"})
@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private TrackService trackService;
    private TagService tagService;
    private ProjectService projectService;
    private DateService dateService;
    private PrimaryFillingLoader primaryFillingLoader;
    private SecurityService securityService;

    private static final int ADMIN_ROLE_ID = 4;
    private static final int MEMBER_ROLE_ID = 1;

    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    @PostMapping("tracks")
    public ResponseEntity<Track> createTrack(@RequestBody Track track) {
        Track savedTrack = trackService.createTrack(track);
        return new ResponseEntity<>(savedTrack, HttpStatus.CREATED);
    }

    @GetMapping("tracks/{id}")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Track> getTrackById(@PathVariable("id") int trackId) {
        Track track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }

    @GetMapping("tracks")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<List<Track>> getAllTracks() {
        List<Track> tracks = trackService.getAllTracks();
        return ResponseEntity.ok(tracks);
    }

    @PostMapping("tags")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.createTag(tag);
        return new ResponseEntity<>(savedTag, HttpStatus.CREATED);
    }

    @GetMapping("tags/{id}")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Tag> getTagById(@PathVariable("id") int tagId) {
        Tag tag = tagService.getTagById(tagId);
        return ResponseEntity.ok(tag);
    }

    @GetMapping("tags")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }

    @PostMapping("projects")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Project> createProject(@ModelAttribute ProjectDTO project) {
        Project savedProject = projectService.createProject(project);
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("projects/{id}")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") int projectId) {
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    @GetMapping("projects")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("projects/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") int projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("Project is deleted");
    }

    @PostMapping("dates")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Date> createDate(@RequestBody Date date) {
        Date saveDate = dateService.createDate(date);
        return new ResponseEntity<>(saveDate, HttpStatus.CREATED);
    }

    @GetMapping("dates/{id}")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Date> getDate(@PathVariable("id")  int dateId) {
        Date date = dateService.getDateById(dateId);
        return ResponseEntity.ok(date);
    }

    @GetMapping("dates")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<List<Date>> getAllDates() {
        List<Date> dates = dateService.getAllDates();
        return ResponseEntity.ok(dates);
    }

    @PostMapping("primary_filling")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<String> processPrimaryFilling(
            //TODO RequestParam vs RequestBody
            @RequestParam("date") Date date,
            @RequestParam("track") Track track,
            @RequestParam("summary") MultipartFile summaryFile,
            @RequestParam("annotation") MultipartFile annotationFile,
            @RequestParam("score") MultipartFile scoreFile){
        //Проверка наличия файлов
        if (summaryFile.isEmpty() || annotationFile.isEmpty() || scoreFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload all 3 CSV file");
        }

        // Проверка расширения файлов
        if ((!summaryFile.getOriginalFilename().toLowerCase().endsWith(".csv")) || (!annotationFile.getOriginalFilename().toLowerCase().endsWith(".csv")) || (!scoreFile.getOriginalFilename().toLowerCase().endsWith(".csv")))  {
            return ResponseEntity.badRequest().body("Only CSV files are allowed");
        }

        try (InputStream summaryStream = summaryFile.getInputStream();
             InputStream annotationStream = annotationFile.getInputStream();
             InputStream scoreStream =scoreFile.getInputStream();) {

            //Первичная заливка
            primaryFillingLoader.load(date.getName(),track.getName(),summaryStream,annotationStream,scoreStream);

            return ResponseEntity.ok("CSV files processed successfully");
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to proccess CSV file" + e.getMessage());
        }

    }


    @PatchMapping("projects/{id}/title")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectTitle(
            @PathVariable("id") int projectId,
            @RequestParam String title) {
        Project updatedProject = projectService.updateProjectTitle(projectId, title);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{id}/description")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectDescription(
            @PathVariable("id") int projectId,
            @RequestParam String description) {
        Project updatedProject = projectService.updateProjectDescription(projectId, description);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{id}/grade")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectGrade(
            @PathVariable("id") int projectId,
            @RequestParam Integer grade) {
        Project updatedProject = projectService.updateProjectGrade(projectId, grade);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{id}/presentation")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectPresentation(
            @PathVariable("id") int projectId,
            @RequestParam String presentation) {
        Project updatedProject = projectService.updateProjectPresentation(projectId, presentation);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{id}/repo")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectRepo(
            @PathVariable("id") int projectId,
            @RequestParam String repo) {
        Project updatedProject = projectService.updateProjectRepo(projectId, repo);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{id}/date")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectDate(
            @PathVariable("id") int projectId,
            @RequestParam int dateId) {
        Project updatedProject = projectService.updateProjectDate(projectId, dateId);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{id}/track")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> updateProjectTrack(
            @PathVariable("id") int projectId,
            @RequestParam int trackId) {
        Project updatedProject = projectService.updateProjectTrack(projectId, trackId);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("projects/{id}/users/add")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> addUserToProject(
            @PathVariable("id") int projectId,
            @RequestParam int userId) {
        Project updatedProject = projectService.addUserToProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("projects/{id}/users/remove")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> removeUserFromProject(
            @PathVariable("id") int projectId,
            @RequestParam int userId) {
        Project updatedProject = projectService.removeUserFromProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("projects/{id}/tags/add")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> addTagToProject(
            @PathVariable("id") int projectId,
            @RequestParam int tagId) {
        Project updatedProject = projectService.addTagToProject(projectId, tagId);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("projects/{id}/tags/remove")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Project> removeTagFromProject(
            @PathVariable("id") int projectId,
            @RequestParam int tagId) {
        Project updatedProject = projectService.removeTagFromProject(projectId, tagId);
        return ResponseEntity.ok(updatedProject);
    }

    @PatchMapping("projects/{projectId}/screenshots")
    @PreAuthorize("@securityService.canEditProject(authentication, #projectId)")
    public ResponseEntity<Void> updateScreenshots(
            @PathVariable int projectId,
            @RequestParam(required = false) List<String> existingImages,
            @RequestParam(required = false) List<MultipartFile> newImages,
            @RequestParam(required = false) MultipartFile mainScreenshotFile,
            @RequestParam(required = false) String mainScreenshotUrl
    ) {
        projectService.updateScreenshots(
                projectId,
                existingImages != null ? existingImages : List.of(),
                newImages != null ? newImages : List.of(),
                mainScreenshotFile,
                mainScreenshotUrl
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admins")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Void> makeAdmin (
            @RequestParam int userId
    ) {
        if(!userService.changeRole(userId, ADMIN_ROLE_ID)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/admins/{userId}")
    @PreAuthorize("@securityService.hasAdminAccess(authentication)")
    public ResponseEntity<Void> makeNotAdmin (
            @PathVariable int userId
    ) {
        if(!userService.changeRole(userId, MEMBER_ROLE_ID)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
