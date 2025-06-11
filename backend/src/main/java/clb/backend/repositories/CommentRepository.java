package clb.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import clb.backend.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProjectIdAndParentCommentIsNull(Long projectId);
}
