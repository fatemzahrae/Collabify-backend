package clb.backend.services;

import clb.backend.entities.Project;
import clb.backend.repositories.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;


    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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



}

