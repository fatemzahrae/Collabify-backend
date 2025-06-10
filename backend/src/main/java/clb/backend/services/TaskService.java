package clb.backend.services;

import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.TaskPriority;
import clb.backend.entities.TaskStatus;
import clb.backend.entities.User;
import clb.backend.mappers.TaskMapper;
import clb.backend.repositories.ProjectRepository;
import clb.backend.repositories.TaskRepository;
import clb.backend.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import clb.backend.DTO.CreateTaskRequest;
import clb.backend.DTO.TaskDTO;
import clb.backend.DTO.UserDataDTO;
import clb.backend.mappers.UserMapper;
import java.time.LocalDateTime;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, 
                      ProjectRepository projectRepository, TaskMapper taskMapper, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskMapper = taskMapper;
        this.userMapper = userMapper;
    }

    private void verifyProjectLeader(Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getLead().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the project leader can perform this action.");
        }
    }

    public TaskDTO createTask(CreateTaskRequest request) {
        if (request.getProjectId() == null) {
            throw new IllegalArgumentException("Task must be assigned to a project.");
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        Task task = taskMapper.toEntity(request);
        task.setProject(project);

        // Set assignee if provided
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
            task.setAssignee(assignee);
        }

        // Set default priority if not provided
        if (task.getPriority() == null) {
            task.setPriority(TaskPriority.MEDIUM);
        }

        // Set creation time
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public TaskDTO findTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        return taskMapper.toDTO(task);
    }

    public TaskDTO updateTask(Long taskId, CreateTaskRequest request) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        verifyProjectLeader(existingTask.getProject());

        taskMapper.updateEntityFromRequest(existingTask, request);

        // Update assignee if provided
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new IllegalArgumentException("Assignee not found"));
            existingTask.setAssignee(assignee);
        } else if (request.getAssigneeId() == null) {
            // If assigneeId is explicitly null in request, remove assignee
            existingTask.setAssignee(null);
        }

        existingTask.setUpdatedAt(LocalDateTime.now());

        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.toDTO(savedTask);
    }

    public TaskDTO updateTaskStatus(Long taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAssignee = task.getAssignee() != null && task.getAssignee().getId().equals(user.getId());
        if (!isAssignee) {
            throw new AccessDeniedException("Only the assignee can update the task status.");
        }

        task.setStatus(newStatus);
        task.setUpdatedAt(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        verifyProjectLeader(task.getProject());
        taskRepository.deleteById(taskId);
    }

    public UserDataDTO findAssignedUser(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        if (task.getAssignee() == null) {
            return null;
        }
        
        return userMapper.toDTO(task.getAssignee());
    }

    public TaskDTO assignUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        verifyProjectLeader(task.getProject());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignee(user);
        task.setUpdatedAt(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public TaskDTO unassignUser(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        
        verifyProjectLeader(task.getProject());

        task.setAssignee(null);
        task.setUpdatedAt(LocalDateTime.now());
        
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    public List<TaskDTO> findAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return taskMapper.toDTOList(tasks);
    }
}