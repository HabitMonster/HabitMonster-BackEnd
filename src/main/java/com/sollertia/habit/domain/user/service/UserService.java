package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.dto.MyPageResponseDto;
import com.sollertia.habit.domain.user.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseVo;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.recommendation.entity.Recommendation;
import com.sollertia.habit.domain.user.recommendation.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
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
    private final RecommendationRepository recommendationRepository;

    public UserInfoResponseDto getUserInfoResponseDto(User user) {
        return UserInfoResponseDto.builder()
                .userInfo(UserInfoVo.of(user))
                .statusCode(200)
                .responseMessage("User Info Query Completed")
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
                .userInfo(UserInfoVo.of(user))
                .statusCode(200)
                .responseMessage("User Name Updated Completed")
                .build();
    }

    @Transactional
    public UserInfoResponseDto disableUser(User user) {
        user.toDisabled();
        UserInfoVo infoVo = UserInfoVo.of(user);

        return UserInfoResponseDto.builder()
                .userInfo(infoVo)
                .statusCode(200)
                .responseMessage("User Droped")
                .build();
    }

    public UserDetailResponseDto getUserDetailDtoByMonsterCode(User user, String monsterCode) {
        User targetUser = findByMonsterCode(monsterCode);

        UserDetailsVo userInfo = getUserDetailsVo(user, targetUser);
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

    public MyPageResponseDto getUserDetailDto(User user) {
        MonsterVo monster = monsterService.getMonsterVo(user);

        return MyPageResponseDto.builder()
                .userInfo(getUserDetailsVo(user))
                .monster(monster)
                .responseMessage("User Info Query Completed")
                .statusCode(200)
                .build();
    }

    private UserDetailsVo getUserDetailsVo(User user) {
        FollowCount followCount = followService.getCountByUser(user);
        Integer totalHabitCount = habitService.getAllHabitCountByUser(user);
        return UserDetailsVo.builder()
                .monsterCode(user.getMonsterCode())
                .username(user.getUsername())
                .email(user.getEmail())
                .totalHabitCount(totalHabitCount)
                .followersCount(followCount.getFollowersCount())
                .followingsCount(followCount.getFollowingsCount())
                .build();
    }

    private UserDetailsVo getUserDetailsVo(User user, User targetUser) {
        boolean isFollowed = followService.isFollowBetween(user, targetUser);
        FollowCount followCount = followService.getCountByUser(targetUser);
        Integer totalHabitCount = habitService.getAllHabitCountByUser(targetUser);
        return UserDetailsVo.builder()
                .monsterCode(targetUser.getMonsterCode())
                .username(targetUser.getUsername())
                .email(targetUser.getEmail())
                .isFollowed(isFollowed)
                .totalHabitCount(totalHabitCount)
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
        List<RecommendationResponseVo> userList = new ArrayList<>();
        List<Recommendation> recommendations = recommendationRepository.findAll();

        for (Recommendation recommendation : recommendations) {
            FollowSearchResponseVo followSearchResponseVo = FollowSearchResponseVo.of(
                    recommendation.getUser(),
                    followService.checkFollow(recommendation.getUser().getMonsterCode(), user).getIsFollowed()
            );
            userList.add(new RecommendationResponseVo(recommendation.getTitle(), followSearchResponseVo));
        }
        return RecommendedUserListDto.builder()
                .userList(userList)
                .responseMessage("Response Recommeded User List")
                .statusCode(200)
                .build();
    }
}
