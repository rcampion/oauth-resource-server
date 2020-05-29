package com.rkc.zds.resource.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rkc.zds.resource.dto.ArticleDto;

public interface ArticleRepository extends JpaRepository<ArticleDto, Integer>, JpaSpecificationExecutor<ArticleDto> {

    ArticleDto save(ArticleDto article);

    Optional<ArticleDto> findById(String id);

    Optional<ArticleDto> findBySlug(String slug);
    
    List<ArticleDto> findByUserId(Integer userId);

    Page<ArticleDto> findByUserId(Pageable page, Integer author);
}
