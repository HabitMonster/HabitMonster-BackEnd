package com.sollertia.habit.testdata;


import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
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

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TestData implements ApplicationRunner {

    private final UserRepository userRepository;
    private final MonsterRepository monsterRepository;
    private final PreSetRepository preSetRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final HabitWithCounterRepository habitWithCounterRepository;
    private final HabitServiceImpl habitService;
    private final MonsterDatabaseRepository monsterDatabaseRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MonsterDatabase monsterDatabase1 = new MonsterDatabase(EvolutionGrade.EV1, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(EvolutionGrade.EV1, "dog.img");
        MonsterDatabase monsterDatabase3 = new MonsterDatabase(EvolutionGrade.EV1, "bird.img");
        monsterDatabaseRepository.save(monsterDatabase1);
        monsterDatabaseRepository.save(monsterDatabase2);
        monsterDatabaseRepository.save(monsterDatabase3);

        User testUser = User.builder().socialId("1234G").username("tester").email("tester.test.com").providerType(ProviderType.GOOGLE).build();
        User testUser2 = User.builder().socialId("123456789G").username("tes").email("tester").providerType(ProviderType.GOOGLE).build();
        User testUser3 = User.builder().socialId("123456789G").username("tes").email("tester").providerType(ProviderType.GOOGLE).build();
        userRepository.save(testUser);
        userRepository.save(testUser2);
        userRepository.save(testUser3);

        User user = userRepository.findById(1L).orElseThrow(()->new NullPointerException("없음"));
        Monster monster = Monster.createNewMonster("고양이", monsterDatabase1);
        monster.setUser(user);
        monster = monsterRepository.save(monster);
        System.out.println("============================");
        System.out.println(monster.getUser().getId());
//        user.updateMonster(monster);
//
//
        User user2 = userRepository.findById(2L).orElseThrow(()->new NullPointerException("없음"));
        Monster monster2 = Monster.createNewMonster("강아지", monsterDatabase2);
        user2.updateMonster(monster2);

        User user3 = userRepository.findById(3L).orElseThrow(()->new NullPointerException("없음"));
        Monster monster3 = Monster.createNewMonster("새", monsterDatabase3);
        user3.updateMonster(monster3);

//        userRepository.save(user);
//        userRepository.save(user2);
//        userRepository.save(user3);

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
            CompletedHabit completedHabit = new CompletedHabit(TestCompletedHabitDto.builder().title(i+"").user(user).isSuccess(true).build());
            completedHabitRepository.save(completedHabit);
        }
        for(int i = 1; i<8; i++){
            CompletedHabit completedHabit = new CompletedHabit(TestCompletedHabitDto.builder().title(i+"").user(user2).isSuccess(true).build());
            completedHabitRepository.save(completedHabit);
        }
        for(int i = 1; i<5; i++){
            CompletedHabit completedHabit = new CompletedHabit(TestCompletedHabitDto.builder().title(i+"").user(user).isSuccess(false).build());
            completedHabitRepository.save(completedHabit);
        }
        for(int i = 1; i<5; i++){
            CompletedHabit completedHabit = new CompletedHabit(TestCompletedHabitDto.builder().title(i+"").user(user2).isSuccess(false).build());
            completedHabitRepository.save(completedHabit);
        }
        for(int i = 1; i<5; i++){
            CompletedHabit completedHabit = new CompletedHabit(TestCompletedHabitDto.builder().title(i+"").user(user3).isSuccess(true).build());
            completedHabitRepository.save(completedHabit);
        }

        // 가장 많이 성공 습관 수행한 유저의 습관 등록 TEST saveAll 사용
        List<PreSetVo> habits = habitWithCounterRepository.findByUser(user).stream().map(PreSetVo::new).collect(Collectors.toCollection(ArrayList::new));
        List<com.sollertia.habit.domain.preset.entity.PreSet> preSets = new ArrayList<>();
        for (PreSetVo h:habits){
            preSets.add(new com.sollertia.habit.domain.preset.entity.PreSet(h));
        }
        preSetRepository.saveAll(preSets);

    }
}
