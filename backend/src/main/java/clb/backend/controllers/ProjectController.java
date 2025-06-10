package clb.backend.controllers;


import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import clb.backend.DTO.CreateProjectRequest;
import clb.backend.DTO.ProjectDTO;
import clb.backend.DTO.TaskDTO;
import clb.backend.DTO.UserDataDTO;
import org.springframework.http.HttpStatus;

@RequestMapping("/api/projects")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody CreateProjectRequest request) {
        ProjectDTO createdProject = projectService.createProject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<Project>> getUserProjects() {
        try {
            List<Project> userProjects = projectService.getUserProjects();
            return ResponseEntity.ok(userProjects);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<ProjectDTO> getProjectByTitle(@PathVariable String title) {
        ProjectDTO project = projectService.getProjectByTitle(title);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody CreateProjectRequest request) {
        ProjectDTO updatedProject = projectService.updateProject(id, request);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<Void> addMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectService.addMember(projectId, memberId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectService.removeMember(projectId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<UserDataDTO>> getMembers(@PathVariable Long projectId) {
        List<UserDataDTO> members = projectService.listMembers(projectId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskDTO>> listTasks(@PathVariable Long projectId) {
        List<TaskDTO> tasks = projectService.listTasks(projectId);
        return ResponseEntity.ok(tasks);
    }
}