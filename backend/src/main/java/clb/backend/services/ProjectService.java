package clb.backend.services;

import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.repositories.ProjectRepository;
import clb.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;

    }

    public Project createProject(Project project) {
         return projectRepository.save(project);
    }
    public Project updateProject(Long projectId, Project updatedProject) {
        return projectRepository.findById(projectId)
                .map(existingProject -> {
                    existingProject.setTitle(updatedProject.getTitle());
                    existingProject.setDescription(updatedProject.getDescription());
                    existingProject.setLead(updatedProject.getLead());
                    existingProject.setMembers(updatedProject.getMembers());
                    return projectRepository.save(existingProject);
                })
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));

    }

    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
    }


    public Project getProjectByTitle(String title) {
        return projectRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with title: " + title));
    }


    // MEMBER OPERATIONS

    public void addMember(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        project.getMembers().add(user);
        projectRepository.save(project);
    }

    public void removeMember(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        project.getMembers().removeIf(member -> member.getId().equals(userId));
        projectRepository.save(project);
    }

    public List<User> listMembers(Long projectId) {
        Project project = getProjectById(projectId);
        return project.getMembers();
    }

    // TASK OPERATIONS


    public List<Task> listTasks(Long projectId) {
        Project project = getProjectById(projectId);
        return project.getTasks();
    }



}

