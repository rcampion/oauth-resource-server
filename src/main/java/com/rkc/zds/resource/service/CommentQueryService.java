package com.rkc.zds.resource.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ArticleCommentDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.CommentData;

@Service
public class CommentQueryService {
    private CommentReadService commentReadService;
    private UserRelationshipQueryService userRelationshipQueryService;

    public CommentQueryService(CommentReadService commentReadService, UserRelationshipQueryService userRelationshipQueryService) {
        this.commentReadService = commentReadService;
        this.userRelationshipQueryService = userRelationshipQueryService;
    }

    public Optional<CommentData> findById(Integer id, UserDto user) {
        CommentData commentData = commentReadService.findById(id, user);
        if (commentData == null) {
            return Optional.empty();
        } else {
            commentData.getProfileData().setFollowing(
                userRelationshipQueryService.isUserFollowing(
                    user.getId(),
                    commentData.getProfileData().getId()));
        }
        return Optional.ofNullable(commentData);
    }

    public List<CommentData> findByArticleId(Integer articleId, UserDto user) {
        List<CommentData> comments = commentReadService.findByArticleId(articleId);
/*
        if (comments.size() > 0 && user != null) {
            Set<String> followingAuthors = userRelationshipQueryService.followingAuthors(user.getId(), comments.stream().map(commentData -> commentData.getProfileData().getId()).collect(Collectors.toList()));
            comments.forEach(commentData -> {
                if (followingAuthors.contains(commentData.getProfileData().getId())) {
                    commentData.getProfileData().setFollowing(true);
                }
            });
        }
*/
        return comments;
    }

	public Optional<ArticleCommentDto> findByArticleIdAndUserId(Integer articleId , Integer userId) {
		return commentReadService.findByArticleIdAndUserId(articleId, userId);
	}
}
