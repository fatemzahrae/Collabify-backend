package clb.backend.controllers;

import clb.backend.DTO.TaskDTO;
import clb.backend.DTO.UserDataDTO;
import clb.backend.entities.Task;
import clb.backend.entities.TaskStatus;
import clb.backend.entities.User;
import clb.backend.services.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long taskId) {
        Task task = taskService.findTaskById(taskId);
        return ResponseEntity.ok(new TaskDTO(task));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        List<TaskDTO> dtos = tasks.stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody Task task) {
        Task updated = taskService.updateTask(taskId, task);
        return ResponseEntity.ok(new TaskDTO(updated));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long taskId, @RequestParam TaskStatus status) {
        Task updated = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(new TaskDTO(updated));
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