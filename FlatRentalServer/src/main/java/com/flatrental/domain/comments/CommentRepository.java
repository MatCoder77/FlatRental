package com.flatrental.domain.comments;

import com.flatrental.domain.managedobject.ManagedObjectState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByAnnouncementIdAndObjectState(Long announcementId, ManagedObjectState objectState);

    List<Comment> getCommentsByUserIdAndObjectState(Long userId, ManagedObjectState objectState);

    Optional<Comment> findCommentByIdAndObjectStateNot(Long id, ManagedObjectState objectState);

    @Query("SELECT c FROM Comment c WHERE (c.id = :id OR c.parentComment = :id) AND c.objectState = com.flatrental.domain.managedobject.ManagedObjectState.ACTIVE")
    List<Comment> getCommentsHierarchy(@Param("id") Long id);

}
