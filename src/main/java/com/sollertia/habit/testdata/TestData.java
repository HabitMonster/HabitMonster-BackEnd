package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.habit.Repository.HabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.habitservice.HabitServiceImpl;
import com.sollertia.habit.domain.monster.EvolutionGrade;
import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.domain.monster.MonsterRepository;
import com.sollertia.habit.domain.preset.PreSetRepository;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.enums.PreSet;
import com.sollertia.habit.domain.user.Level;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.domain.user.UserType;
import com.sollertia.habit.utils.DefaultResponseDto;
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
    private final MonsterRepository monsterRepository;
    private final PreSetRepository preSetRepository;
    private final HabitServiceImpl habitService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Monster monster1 = new Monster(1L, "cat", EvolutionGrade.EV1, "cat.img");
        Monster monster2 = new Monster(2L, "dog", EvolutionGrade.EV1, "dog.img");
        monsterRepository.save(monster1);
        monsterRepository.save(monster2);

        User testUser = User.builder().socialId("1234G").type(UserType.Google).level(Level.LV1).expPoint(0l).monster(monster1).build();
        userRepository.save(testUser);

        User user = userRepository.findById(3L).orElseThrow(()->new NullPointerException("없음"));

        Calendar startDate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < 15; i++) {
            PreSetVo preSetVo = PreSet.getPreSet((long) i);
            assert preSetVo != null;
            com.sollertia.habit.domain.preset.PreSet preSet = new com.sollertia.habit.domain.preset.PreSet(preSetVo);
            preSetRepository.save(preSet);

            Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.DATE, preSetVo.getPeriod());

            HabitDtoImpl habitDto = HabitDtoImpl.builder().durationStart(form.format(startDate.getTime())).durationEnd(form.format(endDate.getTime()))
                    .count(preSetVo.getCount()).title(preSetVo.getTitle()).description(preSetVo.getDescription()).practiceDays(preSetVo.getPracticeDays()).categoryId(preSetVo.getCategoryId()).build();

            HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

            habitService.createHabit(habitTypeDto, habitDto, user);
        }

    }
}
