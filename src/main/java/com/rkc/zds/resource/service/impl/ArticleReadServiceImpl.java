package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ArticleDto;
import com.rkc.zds.resource.dto.ArticleFavoriteDto;
import com.rkc.zds.resource.dto.ArticleTagArticleDto;
import com.rkc.zds.resource.dto.ArticleTagDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.ArticleData;
import com.rkc.zds.resource.model.ArticleDataList;
import com.rkc.zds.resource.model.ProfileData;
import com.rkc.zds.resource.repository.ArticleFavoriteRepository;
import com.rkc.zds.resource.repository.ArticleRepository;
import com.rkc.zds.resource.repository.ArticleTagArticleRepository;
import com.rkc.zds.resource.repository.ArticleTagRepository;
import com.rkc.zds.resource.repository.UserRepository;
import com.rkc.zds.resource.rsql.CustomRsqlVisitor;
import com.rkc.zds.resource.service.ArticleReadService;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;

@Service("articleReadService")
@Qualifier("articleReadService")
public class ArticleReadServiceImpl implements ArticleReadService {

	@Autowired
	private ArticleRepository articleRepo;

	@Autowired
	private ArticleTagRepository articleTagRepo;

	@Autowired
	private ArticleTagArticleRepository articleTagArticleRepo;

	@Autowired
	private ArticleFavoriteRepository articleFavoriteRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public ArticleData findById(Integer id) {
		Optional<ArticleDto> article = articleRepo.findById(id);

		ArticleData data = null;
		ProfileData profile = null;
		ArticleDto articleDto = null;

		if (article.isPresent()) {
			articleDto = article.get();
			data = new ArticleData();

			data.setId(articleDto.getId());
			data.setBody(articleDto.getBody());
			data.setTitle(articleDto.getTitle());
			data.setCreatedAt(articleDto.getCreatedAt());
			data.setUpdatedAt(articleDto.getUpdatedAt());
			data.setDescription(articleDto.getDescription());
			data.setFavorited(false);
			data.setSlug(articleDto.toSlug(articleDto.getTitle()));

			// List<ArticleTagArticleDto> tagDtoList = articleDto.getTagList();
			List<ArticleTagArticleDto> tagDtoList = articleTagArticleRepo.findByArticleId(articleDto.getId());
			List<String> tagList = new ArrayList<String>();
			Optional<ArticleTagDto> tag = null;
			ArticleTagDto tagDto = null;
			for (ArticleTagArticleDto articleTag : tagDtoList) {

				tag = articleTagRepo.findById(articleTag.getTagId());

				if (tag.isPresent()) {
					tagDto = tag.get();
					tagList.add(tagDto.getName());
				}
			}

			data.setTagList(tagList);

			Integer userId = articleDto.getUserId();

			Optional<UserDto> userDto = userRepo.findById(userId);

			if (userDto.isPresent()) {
				UserDto user = userDto.get();

				profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

				data.setProfileData(profile);
			}
		}
		return data;
	}

	@Override
	public ArticleData findBySlug(String slug) {
		Optional<ArticleDto> article = articleRepo.findBySlug(slug);

		ArticleData data = null;
		ProfileData profile = null;
		ArticleDto articleDto = null;

		if (article.isPresent()) {
			articleDto = article.get();
			data = new ArticleData();

			data.setId(articleDto.getId());
			data.setBody(articleDto.getBody());
			data.setTitle(articleDto.getTitle());
			data.setCreatedAt(articleDto.getCreatedAt());
			data.setUpdatedAt(articleDto.getUpdatedAt());
			data.setDescription(articleDto.getDescription());
			data.setFavorited(false);
			data.setSlug(articleDto.toSlug(articleDto.getTitle()));

			List<ArticleTagArticleDto> tagDtoList = articleTagArticleRepo.findByArticleId(articleDto.getId());

			List<String> tagList = new ArrayList<String>();
			Optional<ArticleTagDto> tag = null;
			ArticleTagDto tagDto = null;
			for (ArticleTagArticleDto articleTag : tagDtoList) {

				tag = articleTagRepo.findById(articleTag.getTagId());

				if (tag.isPresent()) {
					tagDto = tag.get();
					tagList.add(tagDto.getName());
				}
			}

			data.setTagList(tagList);

			Integer userId = articleDto.getUserId();

			Optional<UserDto> userDto = userRepo.findById(userId);

			if (userDto.isPresent()) {
				UserDto user = userDto.get();

				profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

				data.setProfileData(profile);
			}
		}
		return data;
	}

	@Override
	public List<String> queryArticles(Pageable pageable, String tag, String author, String favoritedBy) {

		List<String> list = new ArrayList<String>();

		if (tag != null) {
			ArticleTagDto tagDto = articleTagRepo.findByName(tag);
			List<ArticleTagArticleDto> articles = null;

			if (tagDto != null) {
				articles = articleTagArticleRepo.findByTagId(tagDto.getId());
				for (ArticleTagArticleDto element : articles) {
					list.add(element.getArticleId().toString());
				}
				return list;
			}
		}

		List<ArticleDto> articleDtoList = null;
		UserDto userDto = null;
		if (author != null) {
			Optional<UserDto> userDtoTemp = userRepo.findByUserName(author);
			if (userDtoTemp.isPresent()) {
				userDto = userDtoTemp.get();

				articleDtoList = articleRepo.findByUserId(userDto.getId());

				for (ArticleDto article : articleDtoList) {

					list.add(article.getId().toString());

				}

				return list;
			}
		}

		List<ArticleFavoriteDto> favoriteList = null;
		if (favoritedBy != null) {
			Optional<UserDto> userDtoTemp = userRepo.findByUserName(favoritedBy);
			if (userDtoTemp.isPresent()) {
				userDto = userDtoTemp.get();

				favoriteList = articleFavoriteRepo.findByUserId(userDto.getId());

				for (ArticleFavoriteDto favorite : favoriteList) {

					list.add(favorite.getArticleId().toString());

				}

				return list;

			}

		}

		Page<ArticleDto> page = articleRepo.findAll(pageable);

		articleDtoList = page.getContent();

		for (ArticleDto element : articleDtoList) {
			list.add(element.getId().toString());
		}

		return list;
	}

	public Page<ArticleDto> searchArticles(Pageable pageable, Specification<ArticleDto> spec) {
		return articleRepo.findAll(spec, pageable);
	}

	@Override
	public int countArticle(String tag, String author, String favoritedBy) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<ArticleData> findArticles(Pageable pageable, List<String> articleIds) {
		List<ArticleData> list = new ArrayList<ArticleData>();

		ArticleData data = null;
		ProfileData profile = null;

		Optional<ArticleDto> articleDtoTemp = null;

		ArticleDto articleDto = null;

		for (String articleIdString : articleIds) {

			articleDtoTemp = articleRepo.findById(Integer.parseInt(articleIdString));

			if (articleDtoTemp.isPresent()) {
				data = new ArticleData();

				articleDto = articleDtoTemp.get();
				data.setId(articleDto.getId());
				data.setBody(articleDto.getBody());
				data.setTitle(articleDto.getTitle());
				data.setCreatedAt(articleDto.getCreatedAt());
				data.setUpdatedAt(articleDto.getUpdatedAt());
				data.setDescription(articleDto.getDescription());
				data.setFavorited(false);
				data.setSlug(articleDto.getSlug());

				// List<ArticleTagArticleDto> tagDtoList = articleDto.getTagList();
				List<ArticleTagArticleDto> tagDtoList = articleTagArticleRepo.findByArticleId(articleDto.getId());

				List<String> tagList = new ArrayList<String>();
				Optional<ArticleTagDto> tag = null;
				ArticleTagDto tagDto = null;
				for (ArticleTagArticleDto articleTag : tagDtoList) {

					tag = articleTagRepo.findById(articleTag.getTagId());

					if (tag.isPresent()) {
						tagDto = tag.get();
						tagList.add(tagDto.getName());
					}

				}

				data.setTagList(tagList);

				Integer userId = articleDto.getUserId();

				Optional<UserDto> userDto = userRepo.findById(userId);

				if (userDto.isPresent()) {
					UserDto user = userDto.get();

					profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

					data.setProfileData(profile);
				}

				list.add(data);

			}
		}

		return list;
	}

	@Override
	public ArticleDataList findArticlesOfAuthors(Pageable pageable, List<Integer> authors) {

		List<ArticleData> list = new ArrayList<ArticleData>();
		List<ArticleDto> articleDtoList = null;
		ArticleData data = null;
		ProfileData profile = null;

		String search = "";
		for (int i = 0; i < authors.size(); i++) {
			Integer id = authors.get(i);
			if (i == 0) {
				search = "userId==" + id;
			} else {
				search += " or userId==" + id;
			}
		}

		Node rootNode = new RSQLParser().parse(search);
		Specification<ArticleDto> spec = rootNode.accept(new CustomRsqlVisitor<ArticleDto>());

		Page<ArticleDto> page = articleRepo.findAll(spec, pageable);

		for (ArticleDto articleDto : page.getContent()) {
			data = new ArticleData();

			data.setId(articleDto.getId());
			data.setBody(articleDto.getBody());
			data.setTitle(articleDto.getTitle());
			data.setCreatedAt(articleDto.getCreatedAt());
			data.setUpdatedAt(articleDto.getUpdatedAt());
			data.setDescription(articleDto.getDescription());
			data.setFavorited(false);
			data.setSlug(articleDto.getSlug());

			Integer userId = articleDto.getUserId();

			Optional<UserDto> userDto = userRepo.findById(userId);

			if (userDto.isPresent()) {
				UserDto user = userDto.get();

				profile = new ProfileData(user.getId(), user.getUserName(), user.getBio(), user.getImage(), true);

				data.setProfileData(profile);
			}

			list.add(data);
		}
		int size = (int) page.getTotalElements();
		ArticleDataList articleList = new ArticleDataList(list, size);

		return articleList;
	}

	@Override
	public int countFeedSize(List<Integer> authors) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<ArticleDto> findAll(Pageable pageable) {
		return articleRepo.findAll(pageable);
	}

	@Override
	public Page<ArticleDto> findByUserId(Pageable pageable, Integer author) {
		return articleRepo.findByUserId(pageable, author);
	}

	@Override
	public Page<ArticleDto> findFavorites(Pageable pageable, Integer id) {

		Page<ArticleFavoriteDto> favoritesPage = articleFavoriteRepo.findPageByUserId(pageable, id);

		List<ArticleDto> articleDtoList = new ArrayList<ArticleDto>();
		
		for(ArticleFavoriteDto element:favoritesPage.getContent()) {
			Optional<ArticleDto> articleDtoTemp = articleRepo.findById(element.getArticleId());
			if (articleDtoTemp.isPresent()) {
				
				ArticleDto article = articleDtoTemp.get();

				articleDtoList.add(article);
			}
		}
			
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

		PageImpl<ArticleDto> page = new PageImpl<ArticleDto>(articleDtoList, pageRequest,
				favoritesPage.getTotalElements());		

		return page;
	}

	@Override
	public Page<ArticleDto> findByTag(Pageable pageable, String tag) {
		ArticleTagDto tagDto = articleTagRepo.findByName(tag);
		
		Page<ArticleTagArticleDto> articleTagPage = articleTagArticleRepo.findByTagId(pageable, tagDto.getId());

		List<ArticleDto> articleDtoList = new ArrayList<ArticleDto>();
		
		for(ArticleTagArticleDto articleTag:articleTagPage.getContent()) {
			Optional<ArticleDto> articleDtoTemp = articleRepo.findById(articleTag.getArticleId());
			if (articleDtoTemp.isPresent()) {
				
				ArticleDto article = articleDtoTemp.get();

				articleDtoList.add(article);
			}
		}
		
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

		PageImpl<ArticleDto> page = new PageImpl<ArticleDto>(articleDtoList, pageRequest,
				articleTagPage.getTotalElements());		

		return page;
	}

}
