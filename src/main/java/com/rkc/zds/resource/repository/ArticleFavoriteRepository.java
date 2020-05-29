package com.rkc.zds.resource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.resource.dto.ArticleFavoriteDto;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavoriteDto, Integer> {
	ArticleFavoriteDto save(ArticleFavoriteDto articleFavorite);

	Optional<ArticleFavoriteDto> findByArticleIdAndUserId(Integer articleId, Integer userId);
	
	List<ArticleFavoriteDto> findByArticleId(Integer articleId);

	List<ArticleFavoriteDto> findByUserId(Integer id);

	Page<ArticleFavoriteDto> findPageByUserId(Pageable pageable, Integer id);
}
