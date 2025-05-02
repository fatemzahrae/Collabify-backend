package clb.backend.services;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        return taskRepository.findById(taskId)
                .map(existingTask -> {
                    existingTask.setTitle(updatedTask.getTitle());
                    existingTask.setDescription(updatedTask.getDescription());
                    existingTask.setStatus(updatedTask.getStatus());
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task not found with id: " + taskId);
        }
        taskRepository.deleteById(taskId);
    }

    public List<User> findAssignedUsers(Long taskId) {
        Task task = findTaskById(taskId);
        return task.getAssignees();
    }

    public Task addAssignedUser(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        User user = userService.getUserById(userId);
        if (!task.getAssignees().contains(user)) {
            task.getAssignees().add(user);
        }
        return taskRepository.save(task);
    }

    public void deleteAssignedUser(Long taskId, Long userId) {
        Task task = findTaskById(taskId);
        User user = userService.getUserById(userId);
        task.getAssignees().remove(user);
        taskRepository.save(task);
    }
}
