package com.rkc.zds.resource.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ArticleDataList {
    @JsonProperty("articles")
    private List<ArticleData> articleDatas;
    @JsonProperty("articlesCount")
    private int count;

    public ArticleDataList(List<ArticleData> articleDatas, int count) {

        this.articleDatas = articleDatas;
        this.count = count;
    }
    
    public List<ArticleData> getList() {
    	return articleDatas;
    }

	public List<ArticleData> getArticleDatas() {
		return articleDatas;
	}

	public void setArticleDatas(List<ArticleData> articleDatas) {
		this.articleDatas = articleDatas;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
