package clb.backend.DTO;

import java.util.List;

import lombok.Data;

@Data

public class CommentDTO {
    private Long id;
    private String content;
    private String timestamp;
    private boolean edited;
    private Long authorId;
    private List<CommentDTO> replies;
}
