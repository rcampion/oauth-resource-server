package com.rkc.zds.resource.service;

import com.rkc.zds.resource.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface JwtService {
    String toToken(UserDto user);

    Optional<String> getSubFromToken(String token);
}
