package clb.backend.services;

import clb.backend.DTO.DashboardDTO;
import clb.backend.DTO.ProjectDTO;
import clb.backend.DTO.TaskDTO;
import clb.backend.entities.User;
import clb.backend.repositories.ProjectRepository;
import clb.backend.repositories.TaskRepository;
import clb.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public DashboardService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public DashboardDTO getUserDashboard(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<TaskDTO> assignedTasks = taskRepository.findByAssigneesContains(user)
                .stream().map(TaskDTO::new).toList();

        List<ProjectDTO> userProjects = projectRepository.findByMembersContaining(user)
                .stream().map(ProjectDTO::new).toList();

        return new DashboardDTO(assignedTasks, userProjects);
    }
}
