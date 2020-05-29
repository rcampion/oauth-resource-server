package com.rkc.zds.resource.service;

import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.ArticleFavoriteCount;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ArticleFavoritesReadService {
    boolean isUserFavorite(@Param("userId") Integer userId, @Param("articleId") Integer articleId);

    int articleFavoriteCount(@Param("articleId") Integer articleId);

    List<ArticleFavoriteCount> articlesFavoriteCount(@Param("ids") List<Integer> list);

    Set<Integer> userFavorites(@Param("ids") List<Integer> list, @Param("currentUser") UserDto currentUser);
}
