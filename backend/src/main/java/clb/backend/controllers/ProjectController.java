package clb.backend.controllers;


import clb.backend.DTO.ProjectDTO;
import clb.backend.DTO.TaskDTO;
import clb.backend.DTO.UserDataDTO;
import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/projects")
@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {

        this.projectService = projectService;
    }


    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return ResponseEntity.ok(createdProject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(new ProjectDTO(project));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<ProjectDTO>> getProjectByTitle(@PathVariable String title) {
        Project project = projectService.getProjectByTitle(title);
        return ResponseEntity.ok(Collections.singletonList(new ProjectDTO(project)));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        List<ProjectDTO> dtos = projects.stream()
                .map(ProjectDTO::new)
                .collect(Collectors.toList());


        return ResponseEntity.ok(dtos);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(new ProjectDTO(updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ProjectDTO> addMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectService.addMember(projectId, memberId);
        Project project = projectService.getProjectById(projectId);
        return ResponseEntity.ok( new ProjectDTO(project));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<Project> deleteMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectService.removeMember(projectId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/members/")
    public ResponseEntity<List<UserDataDTO>> getMembers(@PathVariable Long projectId) {
        List<User> members =  projectService.listMembers(projectId);
        List<UserDataDTO> dtos = members.stream()
                .map(UserDataDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskDTO>> ListTasks(@PathVariable Long projectId) {
       List<Task> tasks = projectService.listTasks(projectId);
        List<TaskDTO> dtos = tasks.stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

}
