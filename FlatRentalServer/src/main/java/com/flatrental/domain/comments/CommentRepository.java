package com.flatrental.domain.comments;

import com.flatrental.domain.managedobject.ManagedObjectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByAnnouncementIdAndObjectState(Long announcementId, ManagedObjectState objectState);

    Optional<Comment> findCommentByIdAndObjectStateNot(Long id, ManagedObjectState objectState);

    List<Comment> getCommentsByIdOrParentCommentId(Long id, Long parentCommentId);

}
