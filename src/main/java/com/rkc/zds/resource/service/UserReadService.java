package com.rkc.zds.resource.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.rkc.zds.resource.model.UserData;

@Mapper
@Service
public interface UserReadService {

    UserData findByUserName(@Param("userName") String userName);

    UserData findById(@Param("id") Integer integer);
}

