package com.rkc.zds.resource.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PCM_ARTICLE_FAVORITES")
public class ArticleFavoriteDto {
	@Id
	@Column(name="ID", unique = true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="ARTICLE_ID")		
	private Integer articleId;
	
	@Column(name="USER_ID")	
    private Integer userId;

	public ArticleFavoriteDto() {
		
	}
	
    public ArticleFavoriteDto(Integer articleId, Integer userId) {
        this.articleId = articleId;
        this.userId = userId;
    }
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
    public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}


}
