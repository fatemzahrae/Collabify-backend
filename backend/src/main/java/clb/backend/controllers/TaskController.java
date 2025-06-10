package clb.backend.controllers;

import clb.backend.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import clb.backend.DTO.CreateTaskRequest;
import clb.backend.DTO.TaskDTO;
import clb.backend.DTO.UserDataDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody CreateTaskRequest request) {
        TaskDTO created = taskService.createTask(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long taskId) {
        TaskDTO task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody CreateTaskRequest request) {
        TaskDTO updated = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long taskId, @RequestParam String status) {
        TaskDTO updated = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{taskId}/assignee")
    public ResponseEntity<UserDataDTO> getAssignee(@PathVariable Long taskId) {
        UserDataDTO assignee = taskService.findAssignedUser(taskId);
        if (assignee == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(assignee);
    }

    @PostMapping("/{taskId}/assignee/{userId}")
    public ResponseEntity<TaskDTO> assignUserToTask(@PathVariable Long taskId, @PathVariable Long userId) {
        TaskDTO task = taskService.assignUser(taskId, userId);
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}/assignee")
    public ResponseEntity<TaskDTO> unassignUserFromTask(@PathVariable Long taskId) {
        TaskDTO task = taskService.unassignUser(taskId);
        return ResponseEntity.ok(task);
    }
}