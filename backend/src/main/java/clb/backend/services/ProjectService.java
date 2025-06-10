package clb.backend.services;

import clb.backend.DTO.CreateProjectRequest;
import clb.backend.DTO.ProjectDTO;
import clb.backend.DTO.TaskDTO;
import clb.backend.DTO.UserDataDTO;
import clb.backend.entities.Project;
import clb.backend.entities.Task;
import clb.backend.entities.User;
import clb.backend.mappers.ProjectMapper;
import clb.backend.mappers.TaskMapper;
import clb.backend.mappers.UserMapper;
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
    private final ProjectMapper projectMapper;
    private final UserMapper userMapper;
    private final TaskMapper taskMapper;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
                         ProjectMapper projectMapper, UserMapper userMapper, TaskMapper taskMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectMapper = projectMapper;
        this.userMapper = userMapper;
        this.taskMapper = taskMapper;
    }

    private void verifyLeader(Project project) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!project.getLead().getEmail().equals(email)) {
            throw new AccessDeniedException("Only the project leader can modify the project.");
        }
    }

    public ProjectDTO createProject(CreateProjectRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User lead = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        Project project = projectMapper.toEntity(request);
        project.setLead(lead);
        project.getMembers().add(lead);

        // Add additional members if specified
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            List<User> additionalMembers = userRepository.findAllById(request.getMemberIds());
            for (User member : additionalMembers) {
                if (!project.getMembers().contains(member)) {
                    project.getMembers().add(member);
                }
            }
        }

        Project savedProject = projectRepository.save(project);
        return projectMapper.toDTO(savedProject);
    }

    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = project.getMembers().stream().anyMatch(u -> u.getId().equals(user.getId()));
        if (!isMember && !project.getLead().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to view this project.");
        }

        return projectMapper.toDTO(project);
    }

    public ProjectDTO getProjectByTitle(String title) {
        Project project = projectRepository.findByTitle(title)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with title: " + title));
        return projectMapper.toDTO(project);
    }

    public ProjectDTO updateProject(Long projectId, CreateProjectRequest request) {
        Project existingProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id: " + projectId));
        
        verifyLeader(existingProject);
        
        projectMapper.updateEntityFromRequest(existingProject, request);
        
        // Update members if specified
        if (request.getMemberIds() != null) {
            List<User> newMembers = userRepository.findAllById(request.getMemberIds());
            existingProject.getMembers().clear();
            existingProject.getMembers().add(existingProject.getLead()); // Always include lead
            existingProject.getMembers().addAll(newMembers);
        }
        
        Project savedProject = projectRepository.save(existingProject);
        return projectMapper.toDTO(savedProject);
    }

    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new IllegalArgumentException("Project not found with id: " + projectId);
        }
        
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        verifyLeader(project);
        
        projectRepository.deleteById(projectId);
    }

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.toDTOList(projects);
    }

    // MEMBER OPERATIONS

    public void addMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        verifyLeader(project);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!project.getMembers().contains(user)) {
            project.getMembers().add(user);
            projectRepository.save(project);
        }
    }

    public void removeMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        verifyLeader(project);
        
        project.getMembers().removeIf(member -> member.getId().equals(userId));
        projectRepository.save(project);
    }

    public List<UserDataDTO> listMembers(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        // Check access permissions
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = project.getMembers().stream().anyMatch(u -> u.getId().equals(user.getId()));
        if (!isMember && !project.getLead().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to view this project's members.");
        }
        
        return userMapper.toDTOList(project.getMembers());
    }

    public List<Project> getUserProjects() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    
    // Get all projects where the user is either a member or the lead
    return projectRepository.findByMembersContainingOrLead(user, user);
}


    // TASK OPERATIONS

    public List<TaskDTO> listTasks(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        
        // Check access permissions
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = project.getMembers().stream().anyMatch(u -> u.getId().equals(user.getId()));
        if (!isMember && !project.getLead().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to view this project's tasks.");
        }
        
        return taskMapper.toDTOList(project.getTasks());
    }
}