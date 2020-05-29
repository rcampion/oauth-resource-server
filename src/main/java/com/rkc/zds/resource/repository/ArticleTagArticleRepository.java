package com.rkc.zds.resource.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.resource.dto.ArticleTagArticleDto;

public interface ArticleTagArticleRepository extends JpaRepository<ArticleTagArticleDto, Integer>, JpaSpecificationExecutor<ArticleTagArticleDto> {

	List<ArticleTagArticleDto> findByTagId(Integer id);

	ArticleTagArticleDto findByTagIdAndArticleId(Integer tagId, Integer articleId);

	List<ArticleTagArticleDto> findByArticleId(Integer articleId);

	Page<ArticleTagArticleDto> findByTagId(Pageable pageable, Integer id);

}
