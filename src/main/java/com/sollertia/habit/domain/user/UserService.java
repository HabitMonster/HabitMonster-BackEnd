package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import com.sollertia.habit.domain.user.dto.UserInfoVo;
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

    public UserInfoVo getUserInfoVo(User user) {
        return UserInfoVo.builder()
                .socialId(user.getSocialId())
                .email(user.getEmail())
                .username(user.getUsername())
                .socialType(user.getType())
                .build();
    }

    @Transactional
    public User updateMonster(User user, Monster newMonster) {
        user.updateMonster(newMonster);
        return userRepository.save(user);
    }
}
