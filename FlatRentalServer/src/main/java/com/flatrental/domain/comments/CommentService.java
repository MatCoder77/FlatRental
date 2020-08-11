package com.flatrental.domain.comments;

import com.flatrental.domain.announcement.AnnouncementService;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import com.flatrental.infrastructure.utils.Exceptions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AnnouncementService announcementService;
    private final UserService userService;

    public static final String NOT_FOUND = "There is no comment with id {0}";

    public CommentService(CommentRepository commentRepository,
                          @Lazy AnnouncementService announcementService,
                          @Lazy UserService userService) {
        this.commentRepository = commentRepository;
        this.announcementService = announcementService;
        this.userService = userService;
    }

    public List<Comment> getCommentsForAnnouncement(Long announcementId) {
        return commentRepository.getCommentsByAnnouncementIdAndObjectState(announcementId, ManagedObjectState.ACTIVE);
    }

    public Comment createComment(Comment comment) {
        prepareCommentBeforeCreate(comment);
        Comment createdComment = commentRepository.save(comment);
        commentRepository.flush();
        incrementCommentsCounterForAnnouncementIfAddedCommentWasRelatedToAnnouncement(createdComment);
        incrementOpinionsCounterIfAddedCommentWasRelatedToUser(createdComment);
        return createdComment;
    }

    private void prepareCommentBeforeCreate(Comment comment) {
        comment.setObjectState(ManagedObjectState.ACTIVE);
        comment.setLikesCounter(0);
        comment.setDislikesCounter(0);
    }

    private void incrementCommentsCounterForAnnouncementIfAddedCommentWasRelatedToAnnouncement(Comment comment) {
        Optional.ofNullable(comment.getAnnouncement())
                .ifPresent(announcementService::incrementCommentsCounter);
    }

    private void incrementOpinionsCounterIfAddedCommentWasRelatedToUser(Comment comment) {
        Optional.ofNullable(comment.getUser())
                .ifPresent(userService::updateUserStatistics);
    }

    public List<Comment> deleteComment(Long id) {
        List<Comment> commentsToDelete = commentRepository.getCommentsHierarchy(id);
        commentsToDelete.forEach(comment -> comment.setObjectState(ManagedObjectState.REMOVED));
        decrementCommentsCounterForAnnouncementIfRemovedCommentWasRelatedToAnnouncement(commentsToDelete);
        decrementOpinionsCounterForUserIfRemovedCommentWasRelatedToUser(commentsToDelete);
        return commentsToDelete;
    }

    private void decrementCommentsCounterForAnnouncementIfRemovedCommentWasRelatedToAnnouncement(Collection<Comment> removedComments) {
        removedComments.stream()
                .findAny()
                .map(Comment::getAnnouncement)
                .ifPresent(announcement -> announcementService.decrementCommentsCounter(announcement, removedComments.size()));
    }

    private void decrementOpinionsCounterForUserIfRemovedCommentWasRelatedToUser(Collection<Comment> removedOpinions) {
        removedOpinions.stream()
                .findAny()
                .map(Comment::getUser)
                .ifPresent(userService::updateUserStatistics);
    }

    public List<Comment> getCommentsForUser(Long userId) {
        return commentRepository.getCommentsByUserIdAndObjectState(userId, ManagedObjectState.ACTIVE);
    }

    public List<Comment> getCommentsForUser(User user) {
        return commentRepository.getCommentsByUserIdAndObjectState(user.getId(), ManagedObjectState.ACTIVE);
    }

    public Comment getExistingComment(Long id) {
        return commentRepository.findCommentByIdAndObjectStateNot(id, ManagedObjectState.REMOVED)
                .orElseThrow(() -> Exceptions.getObjectNotFoundException(Comment.class, id, ManagedObjectState.getNotRemovedStates()));
    }

}
