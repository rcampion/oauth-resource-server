package com.rkc.zds.resource.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FollowRelation {
	
	private Integer userId;
    private Integer targetId;

    public FollowRelation(Integer userId, Integer targetId) {

        this.userId = userId;
        this.targetId = targetId;
    }
    
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTargetId() {
		return targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}


}
