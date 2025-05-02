package clb.backend.controllers;


import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        Task task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        Task updated = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{taskId}/assignees")
    public ResponseEntity<List<User>> getAssignees(@PathVariable Long taskId) {
        List<User> assignees = taskService.findAssignedUsers(taskId);
        return ResponseEntity.ok(assignees);
    }

    @PostMapping("/{taskId}/assignees/{userId}")
    public ResponseEntity<Task> assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.addAssignedUser(taskId, userId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}/assignees/{userId}")
    public ResponseEntity<Void> unassignUserFromTask(@PathVariable Long taskId, @PathVariable Long userId) {
        taskService.deleteAssignedUser(taskId, userId);
        return ResponseEntity.noContent().build();
    }
}

