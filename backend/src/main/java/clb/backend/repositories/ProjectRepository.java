package clb.backend.repositories;

import clb.backend.entities.Project;
import clb.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTitle(String title);

    List<Project> findByMembersContaining(User user);
}

