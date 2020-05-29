package com.rkc.zds.resource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rkc.zds.resource.dto.ArticleCommentDto;

public interface  ArticleCommentRepository extends JpaRepository<ArticleCommentDto, Integer> {

	ArticleCommentDto save(ArticleCommentDto comment);

    Optional<ArticleCommentDto> findByArticleIdAndUserId(Integer articleId, Integer id);

	List<ArticleCommentDto> findByArticleId(Integer articleId);

	Optional<ArticleCommentDto> findByArticleIdAndId(Integer id, Integer commentId);

}
