package com.flatrental.domain.comments;

import com.flatrental.api.CommentDTO;
import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.announcement.AnnouncementService;
import com.flatrental.domain.managedobject.ManagedObjectService;
import com.flatrental.domain.managedobject.ManagedObjectState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ManagedObjectService managedObjectService;

    @Autowired
    private AnnouncementService announcementService;

    public static final String NOT_FOUND = "There is no comment with id {0}";

    public List<CommentDTO> getCommentsForAnnouncement(Long announcementId) {
        List<Comment> comments = commentRepository.getCommentsByAnnouncementIdAndObjectState(announcementId, ManagedObjectState.ACTIVE);
        Map<Boolean, List<Comment>> commentsByIsRoot = comments.stream()
                .collect(Collectors.partitioningBy(comment -> Objects.isNull(comment.getParentComment())));
        List<Comment> rootComments = commentsByIsRoot.get(Boolean.TRUE);
        List<Comment> subComments = commentsByIsRoot.get(Boolean.FALSE);
        Map<Comment, List<Comment>> commentsByParentCommentId = subComments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getParentComment()));
        return rootComments.stream()
                .map(comment -> mapToCommentDTO(comment, commentsByParentCommentId))
                .collect(Collectors.toList());
    }

    private CommentDTO mapToCommentDTO(Comment comment, Map<Comment, List<Comment>> subCommentsByParentId) {
        List<Comment> subComments = subCommentsByParentId.getOrDefault(comment, Collections.emptyList());
        List<CommentDTO> subCommentDTOs = subComments.stream()
                .map(subComment -> mapToCommentDTO(subComment, subCommentsByParentId))
                .collect(Collectors.toList());
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likesCounter(comment.getLikesCounter())
                .dislikesCounter(comment.getDislikesCounter())
                .announcementId(comment.getAnnouncement().getId())
                .info(managedObjectService.mapToManagedObjectDTO(comment))
                .parentCommentId(Optional.ofNullable(comment.getParentComment()).map(Comment::getId).orElse(null))
                .subcomments(subCommentDTOs)
                .build();
    }

    public Comment createComment(CommentDTO commentDTO) {
        Comment comment = mapToComment(commentDTO);
        comment.setObjectState(ManagedObjectState.ACTIVE);
        comment.setLikesCounter(0);
        comment.setDislikesCounter(0);
        return commentRepository.save(comment);
    }

    private Comment mapToComment(CommentDTO commentDTO) {
        Announcement announcement = announcementService.getExistingAnnouncement(commentDTO.getAnnouncementId());
        Comment parentComment = Optional.ofNullable(commentDTO.getParentCommentId())
                .flatMap(commentRepository::findById)
                .orElse(null);
        return Comment.builder()
                .content(commentDTO.getContent())
                .announcement(announcement)
                .parentComment(parentComment)
                .likesCounter(commentDTO.getLikesCounter())
                .dislikesCounter(commentDTO.getDislikesCounter())
                .build();
    }

    public void deleteComment(Long id) {
        List<Comment> commentsToDelete = commentRepository.getCommentsByIdOrParentCommentId(id, id);
        commentsToDelete.stream()
                .forEach(comment -> comment.setObjectState(ManagedObjectState.REMOVED));
        commentRepository.saveAll(commentsToDelete);
    }

    private Comment getExistingComment(Long id) {
        return commentRepository.findCommentByIdAndObjectStateNot(id, ManagedObjectState.REMOVED)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

}
