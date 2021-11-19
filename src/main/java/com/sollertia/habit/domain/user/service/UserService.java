package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseVo;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowServiceImpl followService;
    private final MonsterService monsterService;
    private final HabitServiceImpl habitService;

    @Transactional
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserIdNotFoundException("Not Found User")
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
                .monsterCode(user.getMonsterCode())
                .email(user.getEmail())
                .username(user.getUsername())
                .socialType(user.getProviderType())
                .monsterName(user.getMonster()==null ? null : user.getMonster().getName())
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

    @Transactional
    public UserInfoResponseDto disableUser(User user) {
        user.toDisabled();
        UserInfoVo infoVo = UserInfoVo.builder()
                .socialType(user.getProviderType())
                .email(user.getEmail())
                .username(user.getUsername())
                .monsterCode(user.getMonsterCode())
                .build();

        return UserInfoResponseDto.builder()
                .userInfo(infoVo)
                .statusCode(200)
                .responseMessage("User Droped")
                .build();
    }

    public UserDetailResponseDto getUserDetailDtoByMonsterCode(User user, String monsterCode) {
        User targetUser = findByMonsterCode(monsterCode);

        UserDetailsVo userInfo = getUserDetailsVo(user, monsterCode, targetUser);
        MonsterVo monster = monsterService.getMonsterVo(targetUser);
        List<HabitSummaryVo> habits = habitService.getHabitListByUser(targetUser);

        return UserDetailResponseDto.builder()
                .userInfo(userInfo)
                .monster(monster)
                .habits(habits)
                .statusCode(200)
                .responseMessage("User Detail Response")
                .build();
    }

    private UserDetailsVo getUserDetailsVo(User user, String monsterCode, User targetUser) {
        boolean isFollowed = followService.isFollowBetween(user, targetUser);
        FollowCount followCount = followService.getCountByUser(targetUser);
        return UserDetailsVo.builder()
                .monsterCode(monsterCode)
                .username(targetUser.getUsername())
                .email(targetUser.getEmail())
                .isFollowed(isFollowed)
                .followersCount(followCount.getFollowersCount())
                .followingsCount(followCount.getFollowingsCount())
                .build();
    }

    private User findByMonsterCode(String monsterCode) {
        return userRepository.findByMonsterCode(monsterCode).orElseThrow(
                () -> new UserIdNotFoundException("Not Found MonsterCode")
        );
    }

    public RecommendedUserListDto getRecommendedUserListDto(User user) {
        List<FollowSearchResponseVo> userList = new ArrayList<>();
        Page<User> userPage = userRepository.findAll(Pageable.ofSize(5));

        for (User target : userPage) {
            userList.add(
                    FollowSearchResponseVo.of(
                            target,
                            followService.checkFollow(target.getMonsterCode(), user).getIsFollowed()
                    ));
        }
        return RecommendedUserListDto.builder()
                .userList(userList)
                    .responseMessage("Response Recommeded User List")
                .statusCode(200)
                .build();
    }
}
