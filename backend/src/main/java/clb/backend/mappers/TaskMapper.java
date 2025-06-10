package clb.backend.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import clb.backend.DTO.CreateTaskRequest;
import clb.backend.DTO.TaskDTO;
import clb.backend.entities.Task;

@Component
public class TaskMapper {

    public TaskDTO toDTO(Task task) {
        if (task == null) {
            return null;
        }

        Long projectId = task.getProject() != null ? task.getProject().getId() : null;
        Long assigneeId = task.getAssignee() != null ? task.getAssignee().getId() : null;

        return new TaskDTO(
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getCreatedAt(),
            task.getUpdatedAt(),
            projectId,
            assigneeId
        );
    }

    public List<TaskDTO> toDTOList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Task toEntity(CreateTaskRequest request) {
        if (request == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        
        return task;
    }

    public void updateEntityFromRequest(Task task, CreateTaskRequest request) {
        if (request != null && task != null) {
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setStatus(request.getStatus());
            task.setPriority(request.getPriority());
        }
    }
}
