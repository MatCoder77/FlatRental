package com.flatrental.domain.comments;

import com.flatrental.api.CommentDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.HasModeratorOrAdminRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Comments")
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    private static final String ID = "id";
    private static final String ID_PATH = "/{" + ID + "}";

    @GetMapping(ID_PATH)
    public List<CommentDTO> getCommentsForAnnouncement(@PathVariable(ID) Long announcementId) {
        List<Comment> comments = commentService.getCommentsForAnnouncement(announcementId);
        return commentMapper.mapToCommentDTOs(comments);
    }

    @GetMapping("/for-profile" + ID_PATH)
    public List<CommentDTO> getCommentsForProfile(@PathVariable(ID) Long userId) {
        List<Comment> comments = commentService.getCommentsForUser(userId);
        return commentMapper.mapToCommentDTOs(comments);
    }

    @PostMapping
    @HasAnyRole
    public ResourceDTO createComment(@Valid @RequestBody CommentDTO commentDTO, @LoggedUser UserInfo userInfo) {
        Comment commentToCreate = commentMapper.mapToComment(commentDTO);
        Comment createdComment = commentService.createComment(commentToCreate);
        return commentMapper.mapToResourceDTO(createdComment);
    }

    @DeleteMapping(ID_PATH)
    @HasModeratorOrAdminRole
    public ResponseDTO deleteComment(@PathVariable(ID) Long id) {
        int numberOfDeletedComments = commentService.deleteComment(id).size();
        return ResponseDTO.builder()
                .success(true)
                .message(String.valueOf(numberOfDeletedComments))
                .build();
    }

}
