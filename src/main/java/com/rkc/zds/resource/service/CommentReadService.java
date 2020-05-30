package com.rkc.zds.resource.service;

import com.rkc.zds.resource.dto.ArticleCommentDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.CommentData;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommentReadService {
    CommentData findById(@Param("id") Integer id, UserDto user);

    List<CommentData> findByArticleId(@Param("articleId") Integer articleId);

    Optional<ArticleCommentDto> findByArticleIdAndUserId(Integer articleId, Integer userId);
}
