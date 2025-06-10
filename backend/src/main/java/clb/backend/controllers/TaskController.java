package clb.backend.controllers;


import clb.backend.entities.Task;
import clb.backend.entities.TaskStatus;
import clb.backend.entities.User;
import clb.backend.services.TaskService;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable Long taskId) {
        Task task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
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

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId, @RequestParam TaskStatus status) {
        Task updated = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{taskId}/assignees")
    public ResponseEntity<User> getAssignees(@PathVariable Long taskId) {
        User assignee = taskService.findAssignedUser(taskId);
        return ResponseEntity.ok(assignee);
    }

    @PostMapping("/{taskId}/assignees/{userId}")
    public ResponseEntity<Task> assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.assignUser(taskId, userId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}/assignees/{userId}")
    public ResponseEntity<Void> unassignUserFromTask(@PathVariable Long taskId, @PathVariable Long userId) {
        taskService.unassignUser(taskId);
        return ResponseEntity.noContent().build();
    }
}
