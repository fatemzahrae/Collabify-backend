package clb.backend.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import clb.backend.DTO.CreateProjectRequest;
import clb.backend.DTO.ProjectDTO;
import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;


@Component
public class ProjectMapper {

    public ProjectDTO toDTO(Project project) {
        if (project == null) {
            return null;
        }

        List<Long> memberIds = project.getMembers() != null ? 
            project.getMembers().stream()
                .map(User::getId)
                .collect(Collectors.toList()) : 
            new ArrayList<>();

        List<Long> taskIds = project.getTasks() != null ? 
            project.getTasks().stream()
                .map(Task::getId)
                .collect(Collectors.toList()) : 
            new ArrayList<>();

        Long leadId = project.getLead() != null ? project.getLead().getId() : null;

        return new ProjectDTO(
            project.getId(),
            project.getTitle(),
            project.getDescription(),
            leadId,
            memberIds,
            taskIds
        );
    }

    public List<ProjectDTO> toDTOList(List<Project> projects) {
        return projects.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Project toEntity(CreateProjectRequest request) {
        if (request == null) {
            return null;
        }

        Project project = new Project();
        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        
        return project;
    }

    public void updateEntityFromRequest(Project project, CreateProjectRequest request) {
        if (request != null && project != null) {
            project.setTitle(request.getTitle());
            project.setDescription(request.getDescription());
        }
    }
}
