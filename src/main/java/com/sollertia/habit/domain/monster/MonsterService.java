package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterSelectRequestDto;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.exception.MonsterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final MonsterRepository monsterRepository;
    private final MonsterCollectionRepository monsterCollectionRepository;
    private final UserRepository userRepository;

    public MonsterListResponseDto getAllMonsters(User user) {
        List<Monster> monsters = monsterRepository.findAllByGrade(EvolutionGrade.EV1);
        if ( user.getMonster() != null ) {
            List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
            for (MonsterCollection monsterCollection : monsterCollectionList) {
                monsters.remove(monsterCollection.getMonster());
            }
            monsters.remove(user.getMonster());
        }
        return MonsterListResponseDto.builder()
                .monsters(monsters)
                .responseMessage("LV1 몬스터를 불러오는데 성공했습니다.")
                .statusCode(200)
                .build();
    }

    public MonsterResponseDto updateMonster(User user,
                                            MonsterSelectRequestDto requestDto) {
        Monster monster = monsterRepository.findById(requestDto.getMonsterId()).orElseThrow(
                () -> new MonsterNotFoundException("올바르지 않은 몬스터 ID입니다.")
        );
        String monsterName = requestDto.getMonsterName();

        if ( user.getMonster() != null ) {
            addMonsterCollection(user, monster);
        }
        user.updateMonster(monster, monsterName);
        userRepository.save(user);

        MonsterVo monsterVo = MonsterVo.of(monster, monsterName);

        return MonsterResponseDto.builder()
                .monster(monsterVo)
                .responseMessage("아바타가 선택되었습니다.")
                .statusCode(200)
                .build();
    }

    public MonsterListResponseDto getMonsterCollection(User user) {
        List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
        List<Monster> monsters = new ArrayList<>();
        for (MonsterCollection monsterCollection : monsterCollectionList) {
            monsters.add(monsterCollection.getMonster());
        }
        return MonsterListResponseDto.builder()
                .monsters(monsters)
                .responseMessage("몬스터 컬렉션 조회 성공")
                .statusCode(200)
                .build();
    }

    public void addMonsterCollection(User user, Monster monster) {
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(user, monster);
        monsterCollectionRepository.save(monsterCollection);
    }

    public MonsterVo getMonsterVo(User user) {
       Monster monster = monsterRepository.findById(user.getMonster().getId()).orElseThrow(
                () -> new MonsterNotFoundException("올바르지 않은 몬스터 ID입니다.")
        );
        return MonsterVo.of(monster, user.getMonsterName());
    }
}
