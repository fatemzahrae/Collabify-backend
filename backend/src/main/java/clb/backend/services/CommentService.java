package clb.backend.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import clb.backend.DTO.CommentDTO;
import clb.backend.DTO.CreateCommentRequest;
import clb.backend.entities.Comment;
import clb.backend.entities.Project;
import clb.backend.entities.User;
import clb.backend.mappers.CommentMapper;
import clb.backend.repositories.CommentRepository;
import clb.backend.repositories.ProjectRepository;
import clb.backend.repositories.UserRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentService(CommentRepository commentRepository,
                          ProjectRepository projectRepository,
                          UserRepository userRepository,
                          CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    public List<CommentDTO> getCommentsByProjectId(Long projectId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Comment> comments = commentRepository.findByProjectIdAndParentCommentIsNull(projectId);
        return comments.stream()
                .map(comment -> commentMapper.toDTO(comment, currentUser))
                .collect(Collectors.toList());
    }

    public CommentDTO addComment(Long projectId, String content, Long parentId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Comment comment = new Comment();
        comment.setAuthorId(user.getId());
        comment.setContent(content);
        comment.setProject(project);
        comment.setTimestamp(Instant.now());
        comment.setEdited(false);

        if (parentId != null) {
            Comment parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParentComment(parent);
        }

        return commentMapper.toDTO(commentRepository.save(comment), user);
    }




    public CommentDTO createComment(CreateCommentRequest request) throws NotFoundException {
    Project project = projectRepository.findById(request.getProjectId())
        .orElseThrow(() -> new NotFoundException());
    User author = userRepository.findById(request.getAuthorId())
        .orElseThrow(() -> new NotFoundException());

    Comment comment = new Comment();
    comment.setContent(request.getContent());
    comment.setAuthorId(author.getId());
    comment.setProject(project);
    comment.setTimestamp(Instant.now());
    comment.setEdited(false);

    if (request.getParentId() != null) {
        Comment parent = commentRepository.findById(request.getParentId())
            .orElseThrow(() -> new NotFoundException());
        comment.setParentComment(parent);
    }

    Comment saved = commentRepository.save(comment);
    return commentMapper.toDTO(saved, author);
}


}
