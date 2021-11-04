package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfoResponseDto(User user) {
        return UserInfoResponseDto.builder()
                .socialId(user.getSocialId())
                .email(user.getEmail())
                .expPercentage(user.getExpPoint())
                .level(user.getLevel().getValue())
                .providerType(user.getProviderType())
                .build();
    }
}
