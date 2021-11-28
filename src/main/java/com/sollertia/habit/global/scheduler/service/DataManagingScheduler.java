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
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.user.entity.Recommendation;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "SCHEDULER_FILE_LOGGER")
@RequiredArgsConstructor
public class DataManagingScheduler {

    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;
    private final HabitRepository habitRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final MonsterRepository monsterRepository;
    private final HistoryRepository historyRepository;
    private final PreSetService preSetService;
    private final PreSetRepository preSetRepository;
    private final RedisUtil redisUtil;
    private final RandomUtil randomUtil;


    public void minusExpOnLapsedHabit(LocalDate date) {
        String day = String.valueOf(date.minusDays(1).getDayOfWeek().getValue());
        List<Habit> habitsWithDaysAndAccomplish = habitRepository.findHabitsWithDaysAndAccomplish(day, false);

        for (Habit habit : habitsWithDaysAndAccomplish) {
            minusExpFromOwnerOf(habit);
            makeHistoryOf(habit);
        }
        habitRepository.updateAccomplishInSessionToFalse();
    }

    private void minusExpFromOwnerOf(Habit habit) {
        Monster monster = monsterRepository.findByUserId(habit.getUser().getId()).orElseThrow(
                () -> new MonsterNotFoundException(habit.getUser().getSocialId() + "의 몬스터를 찾을 수 없습니다."));
        monster.minusExpPoint();
        monsterRepository.saveAndFlush(monster);
    }

    private void makeHistoryOf(Habit habit) {
        History history = History.makeHistory(habit);
        historyRepository.save(history);
    }

    public void expireHabit(LocalDate date) {
        List<Habit> habitListForDelete = habitRepository.findAllByDurationEndLessThan(date);
        log.info("Habit count for delete: " + habitListForDelete.size());

        moveToCompletedHabitList(habitListForDelete);
        deleteHabitList(habitListForDelete);
    }

    private void moveToCompletedHabitList(List<Habit> habitListForDelete) {
        List<CompletedHabit> completedHabitList = CompletedHabit.listOf(habitListForDelete);
        completedHabitRepository.saveAll(completedHabitList);
    }

    private void deleteHabitList(List<Habit> habitListForDelete) {
        List<Long> habitIdListForDelete = habitListForDelete.stream().map(Habit::getId).collect(Collectors.toList());
        habitRepository.deleteAllById(habitIdListForDelete);
    }

    public void makeRecommendations() {
        log.info("Start Remake Recommendations");
        RecommendationType[] values = RecommendationType.values();
        List<Recommendation> recommendationList = new ArrayList<>();

        for (RecommendationType value : values) {
            List<User> top10List = getTop10(value);
            recommendationList.addAll(Recommendation.listOf(top10List, value));
        }
        recommendationRepository.deleteAll();
        recommendationRepository.saveAll(recommendationList);
        log.info("End Remake Recommendations");
    }

    private List<User> getTop10(RecommendationType value) {
        if (value.getId() < 8) {
            return userRepository.searchTop10ByCategory(Category.getCategory(value.getId()));
        } else if (value.getId() == 8) {
            return userRepository.searchTop10ByCategory(null);
        } else if (value.getId() == 9) {
            return userRepository.searchTop10ByFollow();
        } else {
            return new ArrayList<>();
        }
    }

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
