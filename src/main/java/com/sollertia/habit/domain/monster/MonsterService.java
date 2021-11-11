package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.monster.dto.*;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserService;
import com.sollertia.habit.exception.MonsterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final MonsterRepository monsterRepository;
    private final MonsterDatabaseRepository monsterDatabaseRepository;
    private final UserService userService;
    private final MonsterCollectionService monsterCollectionService;

    public MonsterListResponseDto getAllMonsters(User user) {
        List<MonsterDatabase> monsterDatabases = monsterDatabaseRepository.findAllByGrade(EvolutionGrade.EV1);
//        몬스터 빼는 로직
//        if ( user.getMonster() != null ) {
//            List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
//            for (MonsterCollection monsterCollection : monsterCollectionList) {
//                monsterDatabases.remove(monsterCollection.getMonster().getMonsterDatabase());
//            }
//            monsterDatabases.remove(user.getMonster().getMonsterDatabase());
//        }
        return MonsterListResponseDto.builder()
                .monsters(MonsterSummaryVo.listFromMonsterList(monsterDatabases))
                .responseMessage("LV1 몬스터를 불러오는데 성공했습니다.")
                .statusCode(200)
                .build();
    }

    @Transactional
    public MonsterResponseDto updateMonster(User user,
                                            MonsterSelectRequestDto requestDto) {
        String monsterName = requestDto.getMonsterName();
        MonsterDatabase monsterDatabase = getMonsterDatabaseById(requestDto.getMonsterId());

        monsterToCollectionIfExist(user);
        Monster newMonster = Monster.createNewMonster(monsterName, monsterDatabase);
        User updatedUser = userService.updateMonster(user, newMonster);

        MonsterVo monsterVo = MonsterVo.of(updatedUser.getMonster());

        return MonsterResponseDto.builder()
                .monster(monsterVo)
                .responseMessage("몬스터가 선택되었습니다.")
                .statusCode(200)
                .build();
    }

    private void monsterToCollectionIfExist(User user) {
        if ( user.getMonster() != null ) {
            Monster monster = getMonsterByUser(user);
            monsterCollectionService.addMonsterCollection(monster);
        }
    }

    public MonsterResponseDto getMonsterResponseDtoFromUser(User user) {
        Monster monster = getMonsterFromUser(user);
        return MonsterResponseDto.builder()
                .monster(MonsterVo.of(monster))
                .statusCode(200)
                .responseMessage("사용자 몬스터 조회 성공")
                .build();
    }

    public MonsterVo getMonsterVo(User user) {
        Monster monster = getMonsterFromUser(user);
        return MonsterVo.of(monster);
    }

    @Transactional
    public void plusExpPoint(User user) {
        Monster monster = getMonsterFromUser(user);
        monster.plusExpPoint();
    }

    private MonsterDatabase getMonsterDatabaseById(Long id) {
        return monsterDatabaseRepository.findById(id).orElseThrow(
                () -> new MonsterNotFoundException("올바르지 않은 몬스터 ID입니다.")
        );
    }

    private Monster getMonsterFromUser(User user) {
        if ( user.getMonster() == null ) {
            throw new MonsterNotFoundException("아직 몬스터가 없는 사용자입니다.");
        }
        return getMonsterByUser(user);
    }

    private Monster getMonsterByUser(User user) {
        return monsterRepository.findById(user.getMonster().getId()).orElseThrow(
                () -> new MonsterNotFoundException("아직 몬스터가 없는 사용자입니다."));
    }
}