package clb.backend.services;

import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.TaskPriority;
import clb.backend.entities.TaskStatus;
import clb.backend.entities.User;
import clb.backend.repositories.ProjectRepository;
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
    private final ProjectRepository projectRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    private void verifyProjectLeader(Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getLead().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the project leader can perform this action.");
        }
    }

    public Task createTask(Task task) {
        if (task.getProject() == null || task.getProject().getId() == null) {
            throw new IllegalArgumentException("Task must be assigned to a project.");
        }

        Project project = projectRepository.findById(task.getProject().getId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        task.setProject(project);

        if (task.getPriority() == null) {
        task.setPriority(TaskPriority.MEDIUM);
    }
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
        existingTask.setAssignee(updatedTask.getAssignee());
        existingTask.setPriority(updatedTask.getPriority());

        return taskRepository.save(existingTask);
    }

    public Task updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = findTaskById(taskId);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAssignee = task.getAssignee() != null && task.getAssignee().getId().equals(user.getId());
        if (!isAssignee) {
            throw new AccessDeniedException("Only the assignee can update the task status.");
        }

        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId) {
        Task task = findTaskById(taskId);
        verifyProjectLeader(task.getProject());

        taskRepository.deleteById(taskId);
    }

    public User findAssignedUser(Long taskId) {
        Task task = findTaskById(taskId);
        return task.getAssignee();
    }

    public Task assignUser(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        verifyProjectLeader(task.getProject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignee(user);
        return taskRepository.save(task);
    }

    public Task unassignUser(Long taskId) {
        Task task = findTaskById(taskId);
        verifyProjectLeader(task.getProject());

        task.setAssignee(null);
        return taskRepository.save(task);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }
}