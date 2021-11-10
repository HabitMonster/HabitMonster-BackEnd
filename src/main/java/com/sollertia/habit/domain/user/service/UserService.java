package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import com.sollertia.habit.domain.user.dto.UserInfoVo;
import com.sollertia.habit.domain.user.dto.UsernameUpdateRequestDto;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserIdNotFoundException("NotFound User")
        );
    }

    public UserInfoResponseDto getUserInfoResponseDto(User user) {
        return UserInfoResponseDto.builder()
                .userInfo(getUserInfoVo(user))
                .statusCode(200)
                .responseMessage("User Info Query Completed")
                .build();
    }

    private UserInfoVo getUserInfoVo(User user) {
        return UserInfoVo.builder()
                .monsterCode(user.getSocialId())
                .email(user.getEmail())
                .username(user.getUsername())
                .socialType(user.getProviderType())
                .monsterName(user.getMonster().getName())
                .build();
    }

    @Transactional
    public User updateMonster(User user, Monster newMonster) {
        user.updateMonster(newMonster);
        return userRepository.save(user);
    }

    @Transactional
    public UserInfoResponseDto updateUsername(User user, UsernameUpdateRequestDto requestDto) {
        user.updateUsername(requestDto.getUsername());
        return UserInfoResponseDto.builder()
                .userInfo(getUserInfoVo(user))
                .statusCode(200)
                .responseMessage("User Name Updated Completed")
                .build();
    }
}
