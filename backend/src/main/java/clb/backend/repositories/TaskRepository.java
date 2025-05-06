package clb.backend.repositories;

import clb.backend.entities.Task;
import clb.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssigneesContains(User user);
}
