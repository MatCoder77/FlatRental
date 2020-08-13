package com.flatrental.domain.comments;

import com.flatrental.domain.event.comment.CommentAddedEvent;
import com.flatrental.domain.event.comment.CommentRemovedEvent;
import com.flatrental.domain.managedobject.ManagedObjectState;
import com.flatrental.domain.user.User;
import com.flatrental.infrastructure.utils.Exceptions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ApplicationEventPublisher publisher;

    public static final String NOT_FOUND = "There is no comment with id {0}";

    public CommentService(CommentRepository commentRepository,
                          ApplicationEventPublisher publisher) {
        this.commentRepository = commentRepository;
        this.publisher = publisher;
    }

    public List<Comment> getCommentsForAnnouncement(Long announcementId) {
        return commentRepository.getCommentsByAnnouncementIdAndObjectState(announcementId, ManagedObjectState.ACTIVE);
    }

    public Comment createComment(Comment comment) {
        prepareCommentBeforeCreate(comment);
        Comment createdComment = commentRepository.save(comment);
        publishCommentEventsOnCreate(createdComment);
        return createdComment;
    }

    private void prepareCommentBeforeCreate(Comment comment) {
        comment.setObjectState(ManagedObjectState.ACTIVE);
        comment.setLikesCounter(0);
        comment.setDislikesCounter(0);
    }

    private void publishCommentEventsOnCreate(Comment comment) {
        publishEventWithRelatedAnnouncementIfNecessaryOnCreate(comment);
        publishEventWithRelatedUserIfNecessaryOnCreate(comment);
    }

    private void publishEventWithRelatedAnnouncementIfNecessaryOnCreate(Comment comment) {
        Optional.ofNullable(comment.getAnnouncement())
                .map(announcement -> new CommentAddedEvent<>(this, announcement))
                .ifPresent(publisher::publishEvent);
    }

    public void publishEventWithRelatedUserIfNecessaryOnCreate(Comment comment) {
        Optional.ofNullable(comment.getUser())
                .map(user -> new CommentAddedEvent<>(this, user))
                .ifPresent(publisher::publishEvent);
    }

    public List<Comment> deleteComment(Long id) {
        List<Comment> commentsToDelete = commentRepository.getCommentsHierarchy(id);
        commentsToDelete.forEach(comment -> comment.setObjectState(ManagedObjectState.REMOVED));
        publishCommentEventsOnDelete(commentsToDelete);
        return commentsToDelete;
    }

    private void publishCommentEventsOnDelete(Collection<Comment> comments) {
        publishEventWithRelatedAnnouncementIfNecessaryOnDelete(comments);
        publishEventWithRelatedUserIfNecessaryOnDelete(comments);
    }

    private void publishEventWithRelatedAnnouncementIfNecessaryOnDelete(Collection<Comment> comments) {
        Optional.ofNullable(comments)
                .filter(collection -> !collection.isEmpty())
                .flatMap(collection -> collection.stream().findAny())
                .map(Comment::getAnnouncement)
                .map(announcement -> new CommentRemovedEvent<>(this, announcement, getIds(comments)))
                .ifPresent(publisher::publishEvent);
    }

    private List<Long> getIds(Collection<Comment> comments) {
        return comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());
    }

    private void publishEventWithRelatedUserIfNecessaryOnDelete(Collection<Comment> comments) {
        Optional.ofNullable(comments)
                .filter(collection -> !collection.isEmpty())
                .flatMap(collection -> collection.stream().findAny())
                .map(Comment::getUser)
                .map(user -> new CommentRemovedEvent<>(this, user, getIds(comments)))
                .ifPresent(publisher::publishEvent);
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
