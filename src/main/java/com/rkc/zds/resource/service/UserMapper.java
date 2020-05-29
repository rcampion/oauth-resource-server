package com.rkc.zds.resource.service;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.FollowRelation;

@Mapper
public interface UserMapper {
    void insert(@Param("user") UserDto user);

    UserDto findByUserName(@Param("userName") String userName);
    
    UserDto findByEmail(@Param("email") String email);

    UserDto findById(@Param("id") String id);

    void update(@Param("user") UserDto user);

    FollowRelation findRelation(@Param("userId") Integer userId, @Param("targetId") Integer targetId);

    void saveRelation(@Param("followRelation") FollowRelation followRelation);

    void deleteRelation(@Param("followRelation") FollowRelation followRelation);
}
