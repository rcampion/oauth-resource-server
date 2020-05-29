package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ArticleFollowDto;
import com.rkc.zds.resource.repository.ArticleFollowRepository;
import com.rkc.zds.resource.service.UserRelationshipQueryService;

@Service
public class UserRelationshipQueryServiceImpl implements UserRelationshipQueryService {

	@Autowired
	ArticleFollowRepository followRepository;

	@Override
	public boolean isUserFollowing(Integer userId, Integer anotherUserId) {
		ArticleFollowDto dto = followRepository.findByUserIdAndFollowId(userId, anotherUserId);
		if (dto == null) {
			return false;
		}
		return true;
	}

	@Override
	public Set<Integer> followingAuthors(Integer userId, List<Integer> list) {
		Set<Integer> set = new HashSet<Integer>();

		// List<ArticleFollowDto> followDtoList = null;
		ArticleFollowDto followDto = null;
		for (Integer followId : list) {
			followDto = followRepository.findByUserIdAndFollowId(userId, followId);
			if (followDto != null) {
				set.add(followDto.getUserId());
			}

		}
		return set;
	}

	@Override
	public List<Integer> followedUsers(Integer userId) {

		List<Integer> users = new ArrayList<Integer>();
		for (ArticleFollowDto article : followRepository.findByUserId(userId)) {
			users.add(article.getFollowId());
		}
		// add this user to the list
		if(!users.contains(userId)){
			users.add(userId);
		}
		return users;
	}

}
