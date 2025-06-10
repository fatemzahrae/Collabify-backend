package clb.backend.DTO;

import clb.backend.entities.Project;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private UserDataDTO lead;
    private List<UserDataDTO> members;
    private List<TaskDTO> tasks;

    public ProjectDTO(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();

        this.lead = new UserDataDTO(project.getLead());

        this.members = project.getMembers().stream()
                .map(UserDataDTO::new)
                .collect(Collectors.toList());

        this.tasks = project.getTasks().stream()
                .map(TaskDTO::new)
                .collect(Collectors.toList());
    }
}
