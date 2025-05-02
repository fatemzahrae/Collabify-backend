package clb.backend.controllers;


import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Project>> getProjectByTitle(@PathVariable String title) {
        Project project = projectService.getProjectByTitle(title);
        return ResponseEntity.ok(Collections.singletonList(project));
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Project> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<Project> addMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectService.addMember(projectId, memberId);
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<Project> deleteMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectService.removeMember(projectId, memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/members/")
    public ResponseEntity<List<User>> getMembers(@PathVariable Long projectId) {
        List<User> members =  projectService.listMembers(projectId);
        return ResponseEntity.ok(members) ;
    }


    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> ListTasks(@PathVariable Long projectId) {
       List<Task> tasks = projectService.listTasks(projectId);
       return ResponseEntity.ok(tasks) ;
    }

}
