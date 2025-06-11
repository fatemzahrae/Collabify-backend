package clb.backend.controllers;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clb.backend.DTO.CommentDTO;
import clb.backend.DTO.CreateCommentRequest;
import clb.backend.services.CommentService;

@RestController
@RequestMapping("/api/projects/{projectId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@PathVariable Long projectId,
                                                @RequestBody CreateCommentRequest request) throws NotFoundException {
    request.setProjectId(projectId); 
    CommentDTO comment = commentService.createComment(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(comment);
}


    @GetMapping
    public List<CommentDTO> getComments(@PathVariable Long projectId) {
        return commentService.getCommentsByProjectId(projectId);
    }


}
