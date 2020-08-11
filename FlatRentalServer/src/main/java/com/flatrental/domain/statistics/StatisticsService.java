package com.flatrental.domain.statistics;

import com.flatrental.api.UserStatisticsDTO;
import com.flatrental.domain.comments.Comment;
import com.flatrental.domain.comments.CommentService;
import com.flatrental.domain.user.UserStatistics;
import com.flatrental.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CommentService commentService;



    public void updateUserStatistics(User user) {
        List<Comment> allCommentsForUser = commentService.getCommentsForUser(user);
        List<Comment> commentsWithOpinions = allCommentsForUser.stream()
                .filter(this::hasOpinion)
                .collect(Collectors.toList());
        user.getUserStatistics().setCommentsCounter(allCommentsForUser.size());
        user.getUserStatistics().setOpinionsCounter(commentsWithOpinions.size());
        calculateUserRating(commentsWithOpinions).ifPresent(rating -> user.getUserStatistics().setRating(rating));
    }

    private boolean hasOpinion(Comment comment) {
        return comment.getRate() != null;
    }

    private OptionalDouble calculateUserRating(List<Comment> commentsWithOpinions) {
        return commentsWithOpinions.stream()
                .mapToInt(this::calculateRatingForComment)
                .average();
    }

    private int calculateRatingForComment(Comment comment) {
        return comment.getRate();
    }

}
