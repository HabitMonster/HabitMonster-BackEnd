package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.InvalidRecommendationTypeException;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        UserMonsterVo userDetailByMonsterCode = userRepository.userDetailByMonsterCode(monsterCode, user);

        FollowCount followCount = followService.getCountByUser(userDetailByMonsterCode.getUser());
        Integer totalHabitCount = habitService.getAllHabitCountByUser(userDetailByMonsterCode.getUser());

        UserMonsterVo userMonsterVo = new UserMonsterVo();
        userMonsterVo = userMonsterVo.of(userDetailByMonsterCode,followCount,totalHabitCount);

        List<HabitSummaryVo> habits = habitService.getHabitListByUser(userDetailByMonsterCode.getUser());

        return UserDetailResponseDto.builder()
                .userInfo(userMonsterVo.getUserInfo())
                .monster(userMonsterVo.getMonster())
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

    @Transactional
    public RecommendedUserListDto getRecommendedUserListDto(User user) {
        List<RecommendationVo> recommendationVoList = new ArrayList<>();
        int length = 0;
        int count = 0;
        while ( length == 0 ) {
            count++;
            if ( count == 10 ) {
                throw new InvalidRecommendationTypeException("Recommendations List is Empty");
            }
            int number = getRandomNumber();
            recommendationVoList = recommendationRepository.searchByNumber(user, number);
            length = recommendationVoList.size();
        }

        int[] randomNumbers = getRandomNumbers(length);
        List<RecommendationVo> collect = Arrays.stream(randomNumbers)
                .mapToObj(recommendationVoList::get)
                .collect(Collectors.toList());
        return RecommendedUserListDto.builder()
                .userList(collect)
                .responseMessage("Response Recommeded User List")
                .statusCode(200)
                .build();
    }

    private int getRandomNumber() {
        int min = 0;
        int max = 9;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private int[] getRandomNumbers(int max) {
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
