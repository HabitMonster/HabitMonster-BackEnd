package com.sollertia.habit.global.scheduler.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.preset.dto.PreSetDto;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.preset.service.PreSetService;
import com.sollertia.habit.global.utils.RandomUtil;
import com.sollertia.habit.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DataManagingScheduler {

    private final PreSetService preSetService;
    private final PreSetRepository preSetRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final RedisUtil redisUtil;
    private final RandomUtil randomUtil;

    public void makePreset() {

        preSetService.deletePreSet();

        for (int i = 1; i < 8; i++) {
            String key = "PreSet::" + i;
            redisUtil.deleteData(key);
        }

        List<CompletedHabit> completedHabitList = new ArrayList<>();
        Category[] categories = Category.values();
        for (Category c : categories) {
            String achievementPercentage;
            if(redisUtil.hasKey(c.toString())){
                achievementPercentage = redisUtil.getData(c.toString());
            }else{
                achievementPercentage = "70";
            }
            Long achievementPercentageLong = Long.parseLong(achievementPercentage);
            List<CompletedHabit> list = completedHabitRepository.habitMoreThanAvgAchievementPercentageByCategory(c, achievementPercentageLong);

            if(list.size()==0){continue;}

            int size = Math.min(list.size(), 3);
            HashSet<Integer> randomNum = new HashSet<>();
            while(randomNum.size() < size){
                randomNum.add(randomUtil.getRandomNumber(size));
            }
            for (Integer integer : randomNum) {
                completedHabitList.add(list.get(integer));
            }
        }

        if(completedHabitList.size()!=0) {
            List<PreSetDto> habits = completedHabitList.stream()
                    .map(PreSetDto::new).collect(Collectors.toCollection(ArrayList::new));

            List<PreSet> preSets = new ArrayList<>();
            for (PreSetDto h : habits) {
                preSets.add(new PreSet(h));
            }

            preSetRepository.saveAll(preSets);
        }
    }
}
