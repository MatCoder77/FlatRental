package com.flatrental.domain.comments;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.managedobject.ManagedObject;
import com.flatrental.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Comments")
@Data
@EqualsAndHashCode
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends ManagedObject {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "announcement_id")
    private Announcement announcement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "parent_comment_id")
    private Comment parentComment;

    private Integer rate;

    private Integer likesCounter;

    private Integer dislikesCounter;

}
