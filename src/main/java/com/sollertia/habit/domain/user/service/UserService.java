package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.dto.MonsterDto;
import com.sollertia.habit.domain.user.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.InvalidRecommendationTypeException;
import com.sollertia.habit.global.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FollowServiceImpl followService;
    private final HabitServiceImpl habitService;
    private final RecommendationRepository recommendationRepository;
    private final RandomUtil randomUtil;

    public UserInfoResponseDto getUserInfoResponseDto(User user) {
        return UserInfoResponseDto.builder()
                .userInfo(UserInfoDto.of(user))
                .statusCode(200)
                .responseMessage("User Info Query Completed")
                .build();
    }

    @Transactional
    public UserInfoResponseDto updateUsername(User user, UsernameUpdateRequestDto requestDto) {
        user.updateUsername(requestDto.getUsername());
        return UserInfoResponseDto.builder()
                .userInfo(UserInfoDto.of(user))
                .statusCode(200)
                .responseMessage("User Name Updated Completed")
                .build();
    }

    @Transactional
    public UserInfoResponseDto disableUser(User user) {
        user.toDisabled();
        UserInfoDto infoVo = UserInfoDto.of(user);

        return UserInfoResponseDto.builder()
                .userInfo(infoVo)
                .statusCode(200)
                .responseMessage("User Droped")
                .build();
    }

    public UserDetailResponseDto getUserDetailDtoByMonsterCode(User user, String monsterCode) {

        UserMonsterDto userMonsterDto = userRepository.userDetailByMonsterCode(monsterCode, user);
        FollowCount followCount = followService.getCountByUser(userMonsterDto.getUser());
        Integer totalHabitCount = habitService.getAllHabitCountByUser(userMonsterDto.getUser());

        UserDetailsDto userInfoVo = UserDetailsDto.from(userMonsterDto, followCount, totalHabitCount);
        MonsterDto monsterDto = MonsterDto.from(userMonsterDto);
        List<HabitSummaryVo> habits = habitService.getHabitListByUser(userMonsterDto.getUser());

        return UserDetailResponseDto.builder()
                .userInfo(userInfoVo)
                .monster(monsterDto)
                .habits(habits)
                .statusCode(200)
                .responseMessage("User Detail Response")
                .build();
    }

    @Transactional
    public RecommendedUserListDto getRecommendedUserListDto(User user) {
        List<RecommendationDto> recommendationDtoList = new ArrayList<>();
        int length = 0;
        int count = 0;
        while ( length == 0 ) {
            count++;
            if ( count == 10 ) {
                throw new InvalidRecommendationTypeException("Recommendations List is Empty");
            }
            int number = randomUtil.getRandomNumber();
            recommendationDtoList = recommendationRepository.searchByNumber(user, number);
            length = recommendationDtoList.size();
        }

        int[] randomNumbers = randomUtil.getRandomNumbers(length);
        List<RecommendationDto> collect = Arrays.stream(randomNumbers)
                .mapToObj(recommendationDtoList::get)
                .collect(Collectors.toList());
        return RecommendedUserListDto.builder()
                .userList(collect)
                .responseMessage("Response Recommeded User List")
                .statusCode(200)
                .build();
    }
}
