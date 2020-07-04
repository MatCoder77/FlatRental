package com.flatrental.domain.comments;

import com.flatrental.api.CommentDTO;
import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.announcement.AnnouncementService;
import com.flatrental.domain.managedobject.ManagedObjectService;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ManagedObjectService managedObjectService;
    private final AnnouncementService announcementService;
    private final UserService userService;

    public static final String NOT_FOUND = "There is no comment with id {0}";

    public List<CommentDTO> getCommentsForAnnouncement(Long announcementId) {
        List<Comment> comments = commentRepository.getCommentsByAnnouncementIdAndObjectState(announcementId, ManagedObjectState.ACTIVE);
        return mapToCommentDTOs(comments);
    }

    private List<CommentDTO> mapToCommentDTOs(List<Comment> comments) {
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
                .rate(comment.getRate())
                .announcementId(Optional.ofNullable(comment.getAnnouncement()).map(Announcement::getId).orElse(null))
                .userId(Optional.ofNullable(comment.getUser()).map(User::getId).orElse(null))
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
        Comment createdComment = commentRepository.save(comment);
        commentRepository.flush();
        Optional.ofNullable(createdComment.getAnnouncement())
                .ifPresent(announcementService::incrementCommentsCounter);
        Optional.ofNullable(createdComment.getUser())
                .ifPresent(userService::updateUserStatistics);
        return createdComment;
    }

    private Comment mapToComment(CommentDTO commentDTO) {
        Announcement announcement = Optional.ofNullable(commentDTO.getAnnouncementId())
                .map(announcementService::getExistingAnnouncement)
                .orElse(null);
        User user = Optional.ofNullable(commentDTO.getUserId())
                .map(userService::getExistingUser)
                .orElse(null);
        Comment parentComment = Optional.ofNullable(commentDTO.getParentCommentId())
                .flatMap(commentRepository::findById)
                .orElse(null);
        return Comment.builder()
                .content(commentDTO.getContent())
                .announcement(announcement)
                .user(user)
                .parentComment(parentComment)
                .likesCounter(commentDTO.getLikesCounter())
                .dislikesCounter(commentDTO.getDislikesCounter())
                .rate(commentDTO.getRate())
                .build();
    }

    public List<Comment> deleteComment(Long id) {
        List<Comment> commentsToDelete = commentRepository.getCommentsHierarchy(id);
        commentsToDelete.stream()
                .forEach(comment -> comment.setObjectState(ManagedObjectState.REMOVED));
        commentRepository.saveAll(commentsToDelete);
        int numberOfRemovedComments = commentsToDelete.size();
        if (numberOfRemovedComments > 0) {
            Optional.ofNullable(commentsToDelete.get(0).getAnnouncement())
                    .ifPresent(announcement -> announcementService.decrementCommentsCounter(announcement, numberOfRemovedComments));
            Optional.ofNullable(commentsToDelete.get(0).getUser())
                    .ifPresent(userService::updateUserStatistics);
        }
        return commentsToDelete;
    }

    private Comment getExistingComment(Long id) {
        return commentRepository.findCommentByIdAndObjectStateNot(id, ManagedObjectState.REMOVED)
                .orElseThrow(() -> new IllegalArgumentException(MessageFormat.format(NOT_FOUND, id)));
    }

    public List<CommentDTO> getCommentsForUser(Long userId) {
        List<Comment> comments = commentRepository.getCommentsByUserIdAndObjectState(userId, ManagedObjectState.ACTIVE);
        return mapToCommentDTOs(comments);
    }

    public List<Comment> getCommentsForUser(User user) {
        return commentRepository.getCommentsByUserIdAndObjectState(user.getId(), ManagedObjectState.ACTIVE);
    }

}
