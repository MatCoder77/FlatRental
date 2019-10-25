package com.flatrental.domain.comments;

import com.flatrental.api.CommentDTO;
import com.flatrental.api.ResponseDTO;
import com.flatrental.infrastructure.security.HasAnyRole;
import com.flatrental.infrastructure.security.HasModeratorRole;
import com.flatrental.infrastructure.security.LoggedUser;
import com.flatrental.infrastructure.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    private static final String ID = "id";
    private static final String ID_PATH = "/{" + ID + "}";;

    @GetMapping(ID_PATH)
    public List<CommentDTO> getCommentsForAnnouncement(@PathVariable(ID) Long announcementId) {
        return commentService.getCommentsForAnnouncement(announcementId);
    }

    @PostMapping
    @HasAnyRole
    public List<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO, @LoggedUser UserInfo userInfo) {
        commentService.createComment(commentDTO);
        return commentService.getCommentsForAnnouncement(commentDTO.getAnnouncementId());
    }

    @DeleteMapping(ID_PATH)
    @HasModeratorRole
    public ResponseDTO deleteComment(@PathVariable(ID) Long id) {
        int numberOfDeletedComments = commentService.deleteComment(id).size();
        return ResponseDTO.builder()
                .success(true)
                .message(String.valueOf(numberOfDeletedComments))
                .build();
    }

}
