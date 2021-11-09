package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.enums.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


@RequiredArgsConstructor
@Component
public class TestData implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PreSetRepository preSetRepository;
    private final HabitServiceImpl habitService;
    private final MonsterDatabaseRepository monsterDatabaseRepository;
    private final CompletedHabitRepository completedHabitRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MonsterDatabase monsterDatabase1 = new MonsterDatabase(EvolutionGrade.EV1, "https://sollertia.s3.ap-northeast-2.amazonaws.com/image/Lv1_blue.png");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(EvolutionGrade.EV1, "https://sollertia.s3.ap-northeast-2.amazonaws.com/image/Lv1_green.png");
        MonsterDatabase monsterDatabase3 = new MonsterDatabase(EvolutionGrade.EV1, "https://sollertia.s3.ap-northeast-2.amazonaws.com/image/Lv1_orange.png");
        MonsterDatabase monsterDatabase4 = new MonsterDatabase(EvolutionGrade.EV1, "https://sollertia.s3.ap-northeast-2.amazonaws.com/image/Lv1_pink.png");
        MonsterDatabase monsterDatabase5 = new MonsterDatabase(EvolutionGrade.EV1, "https://sollertia.s3.ap-northeast-2.amazonaws.com/image/Lv1_red.png");
        MonsterDatabase monsterDatabase6 = new MonsterDatabase(EvolutionGrade.EV1, "https://sollertia.s3.ap-northeast-2.amazonaws.com/image/Lv1_yellow.png");
        monsterDatabaseRepository.save(monsterDatabase1);
        monsterDatabaseRepository.save(monsterDatabase2);
        monsterDatabaseRepository.save(monsterDatabase3);
        monsterDatabaseRepository.save(monsterDatabase4);
        monsterDatabaseRepository.save(monsterDatabase5);
        monsterDatabaseRepository.save(monsterDatabase6);

        User testUser = User.builder().socialId("1234G").username("tester").email("tester.test.com").providerType(ProviderType.GOOGLE).build();
        userRepository.save(testUser);

        User user = userRepository.findById(1L).orElseThrow(()->new NullPointerException("없음"));
        Monster monster = Monster.createNewMonster("고양이", monsterDatabase1);
        user.updateMonster(monster);
        user = userRepository.save(user);

        Calendar startDate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < 15; i++) {
            PreSetVo preSetVo = PreSet.getPreSet((long) i);
            assert preSetVo != null;
            com.sollertia.habit.domain.preset.entity.PreSet preSet = new com.sollertia.habit.domain.preset.entity.PreSet(preSetVo);
            preSetRepository.save(preSet);

            Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.DATE, preSetVo.getPeriod());

            HabitDtoImpl habitDto = HabitDtoImpl.builder().durationStart(form.format(startDate.getTime())).durationEnd(form.format(endDate.getTime()))
                    .count(preSetVo.getCount()).title(preSetVo.getTitle()).description(preSetVo.getDescription()).practiceDays(preSetVo.getPracticeDays()).categoryId(preSetVo.getCategoryId()).build();

            HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

            habitService.createHabit(habitTypeDto, habitDto, user);
        }

        //CompletedHabit testData
        for(int i = 1; i<15; i++){
            HabitDtoImpl habitDto = HabitDtoImpl.builder().durationStart(form.format(startDate.getTime())).durationEnd(form.format(startDate.getTime()))
                    .count(3).title("test"+i).description("test"+i).practiceDays("1234567").categoryId(Category.Etc.getCategoryId()).build();

            HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");
            CompletedHabit completedHabit = CompletedHabit.of(Habit.createHabit(habitTypeDto.getHabitType(), habitDto, user));
            completedHabitRepository.save(completedHabit);
        }

    }
}
