package com.flatrental.domain.comments;

import com.flatrental.domain.announcement.Announcement;
import com.flatrental.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Comments")
public class Comment {

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    private Long id;

    @OneToOne
    private User author;

    private String content;

    @OneToOne
    private Announcement announcement;

    @OneToOne
    private Comment parentComment;

}
