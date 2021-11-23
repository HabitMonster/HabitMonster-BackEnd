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
import com.sollertia.habit.domain.user.entity.Recommendation;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        int number = getRandomNumber();
        List<Recommendation> recommendations = recommendationRepository.searchByNumber(number);

        int max = recommendations.size();
        int[] vars = getRandom4Numbers(max);

        List<RecommendationVo> userList = new ArrayList<>();
        for (int var : vars) {
            Recommendation recommendation = recommendations.get(var);
            RecommendationVo responseVo = getRecommendationVo(recommendation, user);
            userList.add(responseVo);
        }
        return RecommendedUserListDto.builder()
                .userList(userList)
                .responseMessage("Response Recommeded User List")
                .statusCode(200)
                .build();
    }

    private RecommendationVo getRecommendationVo(Recommendation recommendation, User user) {
        FollowSearchResponseVo followSearchResponseVo = FollowSearchResponseVo.of(
                recommendation.getUser(),
                followService.checkFollow(recommendation.getUser().getMonsterCode(), user).getIsFollowed()
        );
        return new RecommendationVo(recommendation.getType().getTitle(), followSearchResponseVo);
    }

    private int getRandomNumber() {
        int min = 1;
        int max = 10;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private int[] getRandom4Numbers(int max) {
        int size = 5;
        if ( max < size ) {
            size = max;
        }
        Random random = new Random();
        return random.ints(0, max)
                .distinct()
                .limit(size)
                .toArray();
    }
}
