package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.monster.EvolutionGrade;
import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.domain.monster.MonsterRepository;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.domain.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class TestData implements ApplicationRunner {

    private final UserRepository userRepository;
    private final MonsterRepository monsterRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Monster monster1 = new Monster(1L, "cat", EvolutionGrade.EV1, "cat.img");
        Monster monster2 = new Monster(2L, "dog", EvolutionGrade.EV1, "dog.img");
        monsterRepository.save(monster1);
        monsterRepository.save(monster2);

        User testUser = User.builder().socialId("1234G").type(UserType.Google).build();
        userRepository.save(testUser);
    }
}
