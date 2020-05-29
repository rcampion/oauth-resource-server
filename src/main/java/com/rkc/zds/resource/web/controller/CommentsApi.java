package com.rkc.zds.resource.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkc.zds.resource.dto.ArticleCommentDto;
import com.rkc.zds.resource.dto.ArticleDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.exception.NoAuthorizationException;
import com.rkc.zds.resource.exception.ResourceNotFoundException;
import com.rkc.zds.resource.model.CommentData;
import com.rkc.zds.resource.repository.ArticleCommentRepository;
import com.rkc.zds.resource.repository.ArticleRepository;
import com.rkc.zds.resource.repository.UserRepository;
import com.rkc.zds.resource.service.AuthorizationService;
import com.rkc.zds.resource.service.CommentQueryService;

@CrossOrigin(origins = "http://www.zdslogic-development.com:4200")
@RestController

public class CommentsApi {
	private ArticleRepository articleRepository;
	private ArticleCommentRepository commentRepository;
	private CommentQueryService commentQueryService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	public CommentsApi(ArticleRepository articleRepository, ArticleCommentRepository commentRepository,
			CommentQueryService commentQueryService) {
		this.articleRepository = articleRepository;
		this.commentRepository = commentRepository;
		this.commentQueryService = commentQueryService;
	}

	@RequestMapping(value = "/api/articles/{id}/comments", method = RequestMethod.POST, consumes = {
			"application/json;charset=UTF-8" }, produces = { "application/json;charset=UTF-8" })

	public ResponseEntity<?> createComment(@PathVariable("id") Integer id, @RequestBody String jsonString) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		KeycloakPrincipal principal=(KeycloakPrincipal)authentication.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();

		Optional<UserDto> userDto = userRepository.findByUserName(userName);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		ArticleDto article = findArticle(id);

		ArticleCommentDto comment = new ArticleCommentDto();

		ObjectMapper mapper = new ObjectMapper();

		comment.setUserId(user.getId());

		comment.setArticleId(article.getId());

		comment.setBody(jsonString);

		Timestamp stamp = new Timestamp(new Date().getTime());
		comment.setCreatedAt(stamp);
		
		comment = commentRepository.save(comment);

        return ResponseEntity.status(201).body(commentResponse(commentQueryService.findById(comment.getId(), user).get()));	
	}

	@RequestMapping(value = "/api/articles/{articleId}/comments", method = RequestMethod.GET,  produces = { "application/json;charset=UTF-8" })
	public ResponseEntity getComments(@PathVariable("articleId") Integer articleId) {
		
		ArticleDto article = findArticle(articleId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		KeycloakPrincipal principal=(KeycloakPrincipal)authentication.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();

		Optional<UserDto> userDto = userRepository.findByUserName(userName);
		
		UserDto user = null;
		
		if(userDto.isPresent()) {
			user = userDto.get();
		}

		List<CommentData> comments = commentQueryService.findByArticleId(article.getId(), user);
		return ResponseEntity.ok(new HashMap<String, Object>() {
			{
				put("comments", comments);
			}
		});
	}

	@RequestMapping(path = "/api/articles/{articleId}/comments/{commentId}", method = RequestMethod.DELETE)
	public ResponseEntity deleteComment(@PathVariable("articleId") Integer articleId, @PathVariable("commentId") Integer commentId) {
		ArticleDto article = findArticle(articleId);
			
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		KeycloakPrincipal principal=(KeycloakPrincipal)authentication.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        String userName = accessToken.getPreferredUsername();

		Optional<UserDto> userDto = userRepository.findByUserName(userName);
		
		UserDto user = null;
		if(userDto.isPresent()) {
			user = userDto.get();
		}
		
		final UserDto userTemp = user;
		
		return commentRepository.findByArticleIdAndId(article.getId(), commentId).map(comment -> {
			if (!AuthorizationService.canWriteComment(userTemp, article, comment)) {
				throw new NoAuthorizationException();
			}
			commentRepository.delete(comment);
			return ResponseEntity.noContent().build();
		}).orElseThrow(ResourceNotFoundException::new);
	}

	private ArticleDto findArticle(Integer id) {
		return articleRepository.findById(id).map(article -> article).orElseThrow(ResourceNotFoundException::new);
	}

	private Map<String, Object> commentResponse(CommentData commentData) {
		return new HashMap<String, Object>() {

			private static final long serialVersionUID = 1867376745503211112L;

			{
				put("comment", commentData);
			}
		};
	}
}

