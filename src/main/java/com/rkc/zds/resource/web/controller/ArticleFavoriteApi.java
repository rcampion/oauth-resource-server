package com.rkc.zds.resource.web.controller;

import java.util.HashMap;
import java.util.Optional;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rkc.zds.resource.dto.ArticleDto;
import com.rkc.zds.resource.dto.ArticleFavoriteDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.exception.ResourceNotFoundException;
import com.rkc.zds.resource.model.ArticleData;
import com.rkc.zds.resource.repository.ArticleFavoriteRepository;
import com.rkc.zds.resource.repository.ArticleRepository;
import com.rkc.zds.resource.repository.UserRepository;
import com.rkc.zds.resource.service.ArticleQueryService;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController
@RequestMapping(path = "/api/articles/{id}/favorite")
public class ArticleFavoriteApi {
	private ArticleFavoriteRepository articleFavoriteRepository;
	private ArticleRepository articleRepository;
	private ArticleQueryService articleQueryService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	public ArticleFavoriteApi(ArticleFavoriteRepository articleFavoriteRepository, ArticleRepository articleRepository,
			ArticleQueryService articleQueryService) {
		this.articleFavoriteRepository = articleFavoriteRepository;
		this.articleRepository = articleRepository;
		this.articleQueryService = articleQueryService;
	}

	@PostMapping
	public ResponseEntity favoriteArticle(@PathVariable("id") Integer id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		KeycloakPrincipal principal=(KeycloakPrincipal)authentication.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();

		Optional<UserDto> userDto = userRepository.findByUserName(userName);

		UserDto user = null;

		if (userDto.isPresent()) {
			user = userDto.get();
		}

		ArticleDto article = getArticle(id);
		Optional<ArticleFavoriteDto> articleFavoriteTemp = articleFavoriteRepository
				.findByArticleIdAndUserId(article.getId(), user.getId());
		if (!articleFavoriteTemp.isPresent()) {
			ArticleFavoriteDto articleFavorite = new ArticleFavoriteDto(article.getId(), user.getId());
			articleFavoriteRepository.save(articleFavorite);
		}
		return responseArticleData(articleQueryService.findById(id, user).get());
	}

	@DeleteMapping
	public ResponseEntity unfavoriteArticle(@PathVariable("id") Integer id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		KeycloakPrincipal principal=(KeycloakPrincipal)authentication.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();

		Optional<UserDto> userDto = userRepository.findByUserName(userName);

		UserDto user = null;

		if (userDto.isPresent()) {
			user = userDto.get();
		}

		ArticleDto article = getArticle(id);
		articleFavoriteRepository.findByArticleIdAndUserId(article.getId(), user.getId()).ifPresent(favorite -> {
			articleFavoriteRepository.delete(favorite);
		});

		return responseArticleData(articleQueryService.findById(id, user).get());
	}

	private ResponseEntity<HashMap<String, Object>> responseArticleData(final ArticleData articleData) {
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("article", articleData);
			}
		});
	}

	private ArticleDto getArticle(Integer id) {
		return articleRepository.findById(id).map(article -> article).orElseThrow(ResourceNotFoundException::new);
	}
}
