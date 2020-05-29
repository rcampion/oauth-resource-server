package com.rkc.zds.resource.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.dto.ArticleTagDto;
import com.rkc.zds.resource.repository.ArticleTagRepository;
import com.rkc.zds.resource.service.TagReadService;

@Service
public class TagReadServiceImpl implements TagReadService {
	
	@Autowired
	ArticleTagRepository tagRepo;
	
	@Override
	public List<String> all() {
		
		List<ArticleTagDto> dtoList = tagRepo.findAll();
		
		List<String> tagList = new ArrayList<String>(); 
		
		for(ArticleTagDto element:dtoList) {
			tagList.add(element.getName());
		}
		
		return tagList;
	}
}
