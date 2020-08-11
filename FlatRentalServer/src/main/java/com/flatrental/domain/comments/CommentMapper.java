package com.flatrental.domain.comments;

import com.flatrental.api.CommentDTO;
import com.flatrental.api.ResourceDTO;
import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.announcement.AnnouncementService;
import com.flatrental.domain.managedobject.ManagedObjectMapper;
import com.flatrental.domain.user.User;
import com.flatrental.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentMapper {

    private final ManagedObjectMapper managedObjectMapper;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private final CommentService commentService;

    public List<CommentDTO> mapToCommentDTOs(Collection<Comment> comments) {
        Map<Boolean, List<Comment>> partitionedCommentsByIsRoot = comments.stream()
                .collect(Collectors.partitioningBy(this::isRootComment));
        List<Comment> rootComments = partitionedCommentsByIsRoot.get(Boolean.TRUE);
        List<Comment> subComments = partitionedCommentsByIsRoot.get(Boolean.FALSE);
        Map<Comment, List<Comment>> commentsByParentComment = subComments.stream()
                .collect(Collectors.groupingBy(Comment::getParentComment));
        return rootComments.stream()
                .map(comment -> mapToCommentDTO(comment, commentsByParentComment))
                .collect(Collectors.toList());
    }

    private boolean isRootComment(Comment comment) {
        return comment.getParentComment() == null;
    }

    private CommentDTO mapToCommentDTO(Comment comment, Map<Comment, List<Comment>> subCommentsByParentComment) {
        List<Comment> subComments = subCommentsByParentComment.getOrDefault(comment, Collections.emptyList());
        List<CommentDTO> subCommentDTOs = subComments.stream()
                .map(subComment -> mapToCommentDTO(subComment, subCommentsByParentComment))
                .collect(Collectors.toList());
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .likesCounter(comment.getLikesCounter())
                .dislikesCounter(comment.getDislikesCounter())
                .rate(comment.getRate())
                .announcementId(Optional.ofNullable(comment.getAnnouncement())
                        .map(Announcement::getId)
                        .orElse(null))
                .userId(Optional.ofNullable(comment.getUser())
                        .map(User::getId)
                        .orElse(null))
                .info(managedObjectMapper.mapToManagedObjectDTO(comment))
                .parentCommentId(Optional.ofNullable(comment.getParentComment())
                        .map(Comment::getId)
                        .orElse(null))
                .subcomments(subCommentDTOs)
                .build();
    }

    public Comment mapToComment(CommentDTO commentDTO) {
        return Comment.builder()
                .content(commentDTO.getContent())
                .announcement(mapToAnnouncement(commentDTO.getId()))
                .user(mapToUser(commentDTO.getId()))
                .parentComment(mapToParentComment(commentDTO.getParentCommentId()))
                .likesCounter(commentDTO.getLikesCounter())
                .dislikesCounter(commentDTO.getDislikesCounter())
                .rate(commentDTO.getRate())
                .build();
    }

    private Announcement mapToAnnouncement(Long id) {
        return applyMapperOrReturnNullIfArgumentIsNull(id, announcementService::getExistingAnnouncement);
    }

    private <T> T applyMapperOrReturnNullIfArgumentIsNull(Long id, LongFunction<T> mapper) {
        return Optional.ofNullable(id)
                .map(mapper::apply)
                .orElse(null);
    }

    private User mapToUser(Long id) {
        return applyMapperOrReturnNullIfArgumentIsNull(id, userService::getExistingUser);
    }

    private Comment mapToParentComment(Long id) {
        return applyMapperOrReturnNullIfArgumentIsNull(id, commentService::getExistingComment);
    }


    public ResourceDTO mapToResourceDTO(Comment comment) {
        return ResourceDTO.builder()
                .id(comment.getId())
                .build();
    }

}
