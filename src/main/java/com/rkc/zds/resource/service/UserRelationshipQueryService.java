package com.rkc.zds.resource.service;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Mapper
@Service
public interface UserRelationshipQueryService {
    boolean isUserFollowing(@Param("userId") Integer integer, @Param("anotherUserId") Integer integer2);

    Set<Integer> followingAuthors(@Param("userId") Integer userId, @Param("ids") List<Integer> list);

    List<Integer> followedUsers(@Param("userId") Integer integer);
}
