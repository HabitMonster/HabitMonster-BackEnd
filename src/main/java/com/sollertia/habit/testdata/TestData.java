package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
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
    private final MonsterCollectionRepository monsterCollectionRepository;
    private final MonsterRepository monsterRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MonsterDatabase monsterDatabase1 = new MonsterDatabase(EvolutionGrade.EV1, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/4d9d87f2-d9cd-40e6-a24d-6eb1f7f66c00/public");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(EvolutionGrade.EV1, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/9172ee88-5731-447c-4bf6-a18e6af3f500/public");
        MonsterDatabase monsterDatabase3 = new MonsterDatabase(EvolutionGrade.EV1, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/ac0ffaf5-4f0a-48d6-b2f3-81e30ef66600/public");
        MonsterDatabase monsterDatabase4 = new MonsterDatabase(EvolutionGrade.EV1, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/63cbfc9c-6330-4dbe-189c-b8ed524d3600/public");
        MonsterDatabase monsterDatabase5 = new MonsterDatabase(EvolutionGrade.EV1, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/586e5899-007e-4f72-c609-bf374fa6e400/public");
        MonsterDatabase monsterDatabase6 = new MonsterDatabase(EvolutionGrade.EV1, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/0d835d6d-aa79-44ab-17ef-f8f6e1afd200/public");
        MonsterDatabase monsterDatabase7 = new MonsterDatabase(EvolutionGrade.EV2, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/b905774e-cf05-47ab-0ea2-02347b7f9000/public");
        MonsterDatabase monsterDatabase8 = new MonsterDatabase(EvolutionGrade.EV3, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/f57d212a-5321-45ae-2626-da36f0244a00/public");
        MonsterDatabase monsterDatabase9 = new MonsterDatabase(EvolutionGrade.EV4, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/bcf97df5-0a07-4873-4fa9-4d3b2a1f7a00/public");
        MonsterDatabase monsterDatabase10 = new MonsterDatabase(EvolutionGrade.EV5, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/ea39b10c-f522-4e6b-a754-28f17fa83d00/public");

        Monster monster1 = Monster.createNewMonster("돼지", monsterDatabase4);
        Monster monster2 = Monster.createNewMonster("돼지", monsterDatabase7);
        Monster monster3 = Monster.createNewMonster("돼지", monsterDatabase8);
        Monster monster4 = Monster.createNewMonster("돼지", monsterDatabase9);
        Monster monster5 = Monster.createNewMonster("돼지", monsterDatabase10);



        monsterDatabaseRepository.save(monsterDatabase1);
        monsterDatabaseRepository.save(monsterDatabase2);
        monsterDatabaseRepository.save(monsterDatabase3);
        monsterDatabaseRepository.save(monsterDatabase4);
        monsterDatabaseRepository.save(monsterDatabase5);
        monsterDatabaseRepository.save(monsterDatabase6);
        monsterDatabaseRepository.save(monsterDatabase7);
        monsterDatabaseRepository.save(monsterDatabase8);
        monsterDatabaseRepository.save(monsterDatabase9);
        monsterDatabaseRepository.save(monsterDatabase10);

        MonsterCollection monsterCollection;



        User testUser = User.builder().socialId("1234G").username("tester").email("tester.test.com").providerType(ProviderType.GOOGLE).build();
        userRepository.save(testUser);

        User user = userRepository.findById(1L).orElseThrow(()->new NullPointerException("없음"));

        user.updateMonster(monster1);
        user = userRepository.save(user);
        monster1 = monsterRepository.save(monster1);
        monsterCollection = MonsterCollection.createMonsterCollection(monster1);
        monsterCollectionRepository.save(monsterCollection);
        user.updateMonster(monster2);
        user = userRepository.save(user);
        monster2.levelUp();
        monster2 = monsterRepository.save(monster2);
        monsterCollection = MonsterCollection.createMonsterCollection(monster2);
        monsterCollectionRepository.save(monsterCollection);
        user.updateMonster(monster3);
        user = userRepository.save(user);
        monster3.levelUp();monster3.levelUp();
        monster3 = monsterRepository.save(monster3);
        monsterCollection = MonsterCollection.createMonsterCollection(monster3);
        monsterCollectionRepository.save(monsterCollection);
        user.updateMonster(monster4);
        user = userRepository.save(user);
        monster4.levelUp();monster4.levelUp();monster4.levelUp();
        monster4 = monsterRepository.save(monster4);
        monsterCollection = MonsterCollection.createMonsterCollection(monster4);
        monsterCollectionRepository.save(monsterCollection);
        user.updateMonster(monster5);
        user = userRepository.save(user);
        monster5.levelUp();monster5.levelUp();monster5.levelUp();monster5.levelUp();
        monster5 = monsterRepository.save(monster5);
        monsterCollection = MonsterCollection.createMonsterCollection(monster5);
        monsterCollectionRepository.save(monsterCollection);



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
