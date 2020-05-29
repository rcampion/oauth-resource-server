package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ArticleFavoriteDto;
import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.ArticleFavoriteCount;
import com.rkc.zds.resource.repository.ArticleFavoriteRepository;
import com.rkc.zds.resource.service.ArticleFavoritesReadService;

@Service("articleFavoritesReadService")
@Qualifier("articleFavoritesReadService")
public class ArticleFavoritesReadServiceImpl implements ArticleFavoritesReadService {

	@Autowired
	ArticleFavoriteRepository favoritesRepo;
	
	@Override
	public boolean isUserFavorite(Integer userId, Integer articleId) {
		Optional<ArticleFavoriteDto> dto = favoritesRepo.findByArticleIdAndUserId(articleId, userId);
		if(dto.isPresent()) {
			return true;
		}
		return false;
	}

	@Override
	public int articleFavoriteCount(Integer articleId) {
		List<ArticleFavoriteDto> favoriteDtoList = favoritesRepo.findByArticleId(articleId);
		return favoriteDtoList.size();
	}

	@Override
	public List<ArticleFavoriteCount> articlesFavoriteCount(List<Integer> list) {
		
		List<ArticleFavoriteCount> favoriteList = new ArrayList<ArticleFavoriteCount>();

		ArticleFavoriteCount count = null;
		ArticleFavoriteDto favorite = null;
		List<ArticleFavoriteDto> favoriteDtoList;
		
		int incrementor = 0;
		
		for(Integer id: list) {
	
			count = new ArticleFavoriteCount();
			count.setId(id);
			count.setCount(0);
			favoriteDtoList = favoritesRepo.findByArticleId(id);
			incrementor = 0;
			for(ArticleFavoriteDto favoriteDto : favoriteDtoList) {
		
				incrementor++;

			}
			count.setCount(incrementor);
			favoriteList.add(count);
		}
		return favoriteList;
	}

	@Override
	public Set<Integer> userFavorites(List<Integer> list, UserDto currentUser) {
		
		Set<Integer> set = new HashSet<Integer>();
		
		List<ArticleFavoriteDto> favoriteDtoList = null;
		
		for(Integer id: list) {
			favoriteDtoList = favoritesRepo.findByArticleId(id);
			for(ArticleFavoriteDto dto: favoriteDtoList) {
				set.add(dto.getArticleId());
			}
		}
		return set;
	}

}
