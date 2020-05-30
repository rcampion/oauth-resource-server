package com.rkc.zds.resource.service;

import org.apache.ibatis.annotations.Param;

import com.rkc.zds.resource.model.UserData;

public interface UserReadService {

    UserData findByUserName(@Param("userName") String userName);

    UserData findById(@Param("id") Integer integer);
}

