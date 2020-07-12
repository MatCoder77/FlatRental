package com.flatrental.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {

    private Long id;
    private String content;
    private Long parentCommentId;
    private List<CommentDTO> subcomments;
    private Integer likesCounter;
    private Integer dislikesCounter;
    private Integer rate;
    Long announcementId;
    Long userId;
    private ManagedObjectDTO info;

}
