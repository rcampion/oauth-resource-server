package com.rkc.zds.resource.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rkc.zds.resource.model.UserData;

@Service
public class UserQueryService  {
    private UserReadService userReadService;

    public UserQueryService(UserReadService userReadService) {
        this.userReadService = userReadService;
    }

    public Optional<UserData> findById(Integer integer) {
        return Optional.ofNullable(userReadService.findById(integer));
    }
}

