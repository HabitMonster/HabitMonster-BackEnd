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
        Monster monster = new Monster(1L, "cat", EvolutionGrade.EV1, "cat.img");
        monsterRepository.save(monster);

        User testUser = User.builder().socialId("1234G").type(UserType.Google).build();
        testUser.updateMonster(monster, "test monster");
        userRepository.save(testUser);
    }
}
