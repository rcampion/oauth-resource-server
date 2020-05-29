package com.rkc.zds.resource.service;

import com.rkc.zds.resource.dto.ArticleCommentDto;
import com.rkc.zds.resource.dto.ArticleDto;
import com.rkc.zds.resource.dto.UserDto;

public class AuthorizationService {
    public static boolean canWriteArticle(UserDto user, ArticleDto article) {
        return user.getId().equals(article.getUserId());
    }

    public static boolean canWriteComment(UserDto user, ArticleDto article, ArticleCommentDto comment) {
        return user.getId().equals(article.getUserId()) || user.getId().equals(comment.getUserId());
    }
}
