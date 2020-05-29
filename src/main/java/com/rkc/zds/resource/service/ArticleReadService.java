package com.rkc.zds.resource.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.rkc.zds.resource.dto.ArticleDto;
import com.rkc.zds.resource.model.ArticleData;
import com.rkc.zds.resource.model.ArticleDataList;

@Mapper
public interface ArticleReadService {
    ArticleData findById(@Param("id") Integer id);

    ArticleData findBySlug(@Param("slug") String slug);

    List<String> queryArticles(Pageable pageable, @Param("tag") String tag, @Param("author") String author, @Param("favoritedBy") String favoritedBy);

    int countArticle(@Param("tag") String tag, @Param("author") String author, @Param("favoritedBy") String favoritedBy);

    List<ArticleData> findArticles(Pageable pageable, @Param("articleIds") List<String> articleIds);

    ArticleDataList findArticlesOfAuthors(Pageable pageable, @Param("authors") List<Integer> authors);

    int countFeedSize(@Param("authors") List<Integer> authors);

	Page<ArticleDto> findAll(Pageable pageable);

	Page<ArticleDto> findByUserId(Pageable pageable, Integer id);
		
	Page<ArticleDto> searchArticles(Pageable pageable, Specification<ArticleDto> spec);

	Page<ArticleDto> findFavorites(Pageable pageable, Integer id);
	
	Page<ArticleDto> findByTag(Pageable pageable, String tag);

}
