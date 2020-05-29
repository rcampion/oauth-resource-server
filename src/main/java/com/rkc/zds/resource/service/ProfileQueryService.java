package com.rkc.zds.resource.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkc.zds.resource.dto.UserDto;
import com.rkc.zds.resource.model.ProfileData;
import com.rkc.zds.resource.model.UserData;

@Component
public class ProfileQueryService {
    private UserReadService userReadService;
    private UserRelationshipQueryService userRelationshipQueryService;

    @Autowired
    public ProfileQueryService(UserReadService userReadService, UserRelationshipQueryService userRelationshipQueryService) {
        this.userReadService = userReadService;
        this.userRelationshipQueryService = userRelationshipQueryService;
    }

    public Optional<ProfileData> findByUserName(String userName, UserDto currentUser) {
        UserData userData = userReadService.findByUserName(userName);
        if (userData == null) {
            return Optional.empty();
        } else {
            ProfileData profileData = new ProfileData(
                userData.getId(),
                userData.getUserName(),
                userData.getBio(),
                userData.getImage(),
                userRelationshipQueryService.isUserFollowing(currentUser.getId(), userData.getId()));
            return Optional.of(profileData);
        }
    }
}
