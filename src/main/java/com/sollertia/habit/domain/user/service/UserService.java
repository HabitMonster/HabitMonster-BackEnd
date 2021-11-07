package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import com.sollertia.habit.domain.user.dto.UserInfoVo;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getUserInfoResponseDto(User user) {
        return UserInfoResponseDto.builder()
                .userInfo(getUserInfoVo(user))
                .statusCode(200)
                .responseMessage("사용자 정보 조회 성공")
                .build();
    }

    private UserInfoVo getUserInfoVo(User user) {
        return UserInfoVo.builder()
                .monsterCode(user.getSocialId())
                .email(user.getEmail())
                .username(user.getUsername())
                .socialType(user.getProviderType())
                .build();
    }

    @Transactional
    public User updateMonster(User user, Monster newMonster) {
        user.updateMonster(newMonster);
        return userRepository.save(user);
    }
}
