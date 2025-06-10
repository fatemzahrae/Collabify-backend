package clb.backend.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DashboardDTO {
    private List<TaskDTO> assignedTasks;
    private List<ProjectDTO> userProjects;

    public DashboardDTO(List<TaskDTO> assignedTasks, List<ProjectDTO> userProjects) {
        this.assignedTasks = assignedTasks;
        this.userProjects = userProjects;
    }

}
