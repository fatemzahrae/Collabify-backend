package clb.backend.services;

import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.TaskStatus;
import clb.backend.entities.User;
import clb.backend.repositories.TaskRepository;
import clb.backend.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;

        this.userRepository = userRepository;
    }

    private void verifyProjectLeader(Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getLead().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the project leader can perform this action.");
        }
    }

    public Task createTask(Task task) {
        Project project = task.getProject();
        verifyProjectLeader(project);
        return taskRepository.save(task);
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task existingTask = findTaskById(taskId);
        verifyProjectLeader(existingTask.getProject());

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        return taskRepository.save(existingTask);
    }

    public Task updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = findTaskById(taskId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAssignee = task.getAssignees().stream().anyMatch(u -> u.getId().equals(user.getId()));
        if (!isAssignee) {
            throw new AccessDeniedException("Only assignees can update the task status.");
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        Task task = findTaskById(taskId);
        verifyProjectLeader(task.getProject());

        taskRepository.deleteById(taskId);
    }

    public List<User> findAssignedUsers(Long taskId) {
        Task task = findTaskById(taskId);
        return task.getAssignees();
    }

    public Task addAssignedUser(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        verifyProjectLeader(task.getProject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!task.getAssignees().contains(user)) {
            task.getAssignees().add(user);
        }
        return taskRepository.save(task);
    }

    public void deleteAssignedUser(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        verifyProjectLeader(task.getProject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.getAssignees().remove(user);
        taskRepository.save(task);
    }
}
