package clb.backend.services;

import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.repositories.ProjectRepository;
import clb.backend.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private void verifyLeader(Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getLead().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the project leader can modify the project.");
        }
    }


    public Project createProject(Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User lead = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        project.setLead(lead);
        project.getMembers().add(lead);
        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = project.getMembers().stream().anyMatch(u -> u.getId().equals(user.getId()));
        if (!isMember && !project.getLead().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to view this project.");
        }

        return project;
    }



    public Project getProjectByTitle(String title) {
        return projectRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with title: " + title));
    }


    public Project updateProject(Long projectId, Project updatedProject) {
        verifyLeader(getProjectById(projectId));
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
        verifyLeader(getProjectById(projectId));
        projectRepository.deleteById(projectId);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }



    // MEMBER OPERATIONS

    public void addMember(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        verifyLeader(project);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!project.getMembers().contains(user)) {
            project.getMembers().add(user);
            projectRepository.save(project);
        }
    }


    public void removeMember(Long projectId, Long userId) {
        Project project = getProjectById(projectId);
        verifyLeader(project);
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

