package clb.backend.mappers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import clb.backend.DTO.CommentDTO;
import clb.backend.entities.Comment;
import clb.backend.entities.User;
import clb.backend.repositories.UserRepository;
@Component
public class CommentMapper {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public CommentMapper(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository; 
    }

    public CommentDTO toDTO(Comment comment, User currentUser) {
        if (comment == null) return null;

        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setTimestamp(comment.getTimestamp().toString());
        dto.setEdited(comment.isEdited());

        // ✅ Set authorId directly
        dto.setAuthorId(comment.getAuthorId());

        dto.setReplies(comment.getReplies().stream()
            .map(reply -> toDTO(reply, currentUser))
            .collect(Collectors.toList()));

        return dto;
    }
}
