package clb.backend.repositories;

import clb.backend.entities.Project;
import clb.backend.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTitle(String title);
    @Query("SELECT p FROM Project p WHERE :user MEMBER OF p.members OR p.lead = :lead")
    List<Project> findByMembersContainingOrLead(@Param("user") User user, @Param("lead")User lead);

}

