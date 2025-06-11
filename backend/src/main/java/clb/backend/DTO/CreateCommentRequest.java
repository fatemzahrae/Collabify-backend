package clb.backend.DTO;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String content;
    private Long projectId;
    private Long authorId;
    private Long parentId; 
}
