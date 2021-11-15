package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.entity.*;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.notice.dto.NoticeVo;
import com.sollertia.habit.domain.notice.entiy.Notice;
import com.sollertia.habit.domain.notice.repository.NoticeRepository;
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
    private final MonsterCollectionDatabaseRepository monsterCollectionDatabaseRepository;
    private final MonsterRepository monsterRepostory;
    private final NoticeRepository noticeRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE,"https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/4d9d87f2-d9cd-40e6-a24d-6eb1f7f66c00/public");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(Level.LV1, MonsterType.PINK,"https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/9172ee88-5731-447c-4bf6-a18e6af3f500/public");
        MonsterDatabase monsterDatabase3 = new MonsterDatabase(Level.LV1, MonsterType.BLUE,"https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/ac0ffaf5-4f0a-48d6-b2f3-81e30ef66600/public");
        MonsterDatabase monsterDatabase4 = new MonsterDatabase(Level.LV1, MonsterType.YELLOW,"https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/63cbfc9c-6330-4dbe-189c-b8ed524d3600/public");
        MonsterDatabase monsterDatabase5 = new MonsterDatabase(Level.LV1, MonsterType.ORANGE, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/586e5899-007e-4f72-c609-bf374fa6e400/public");
        MonsterDatabase monsterDatabase6 = new MonsterDatabase(Level.LV1, MonsterType.RED,"https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/0d835d6d-aa79-44ab-17ef-f8f6e1afd200/public");

        MonsterDatabase monsterDatabase7 = new MonsterDatabase(Level.LV2, MonsterType.YELLOW, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/b905774e-cf05-47ab-0ea2-02347b7f9000/public");
        MonsterDatabase monsterDatabase8 = new MonsterDatabase(Level.LV3, MonsterType.YELLOW, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/f57d212a-5321-45ae-2626-da36f0244a00/public");
        MonsterDatabase monsterDatabase9 = new MonsterDatabase(Level.LV4, MonsterType.YELLOW, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/bcf97df5-0a07-4873-4fa9-4d3b2a1f7a00/public");
        MonsterDatabase monsterDatabase10 = new MonsterDatabase(Level.LV5, MonsterType.YELLOW, "https://imagedelivery.net/jUjG58o6h5HnOZzG5_k-ng/ea39b10c-f522-4e6b-a754-28f17fa83d00/public");

        Monster monster = Monster.createNewMonster("돼지", monsterDatabase4);

        monsterDatabaseRepository.save(monsterDatabase1);
        monsterDatabaseRepository.save(monsterDatabase2);
        monsterDatabaseRepository.save(monsterDatabase3);
        monsterDatabase4 = monsterDatabaseRepository.save(monsterDatabase4);
        monsterDatabaseRepository.save(monsterDatabase5);
        monsterDatabaseRepository.save(monsterDatabase6);
        monsterDatabaseRepository.save(monsterDatabase7);
        monsterDatabaseRepository.save(monsterDatabase8);
        monsterDatabaseRepository.save(monsterDatabase9);
        monsterDatabaseRepository.save(monsterDatabase10);

        MonsterCollection monsterCollection;
        MonsterCollectionDatabase monsterCollectionDatabase;


        User testUser = User.builder().socialId("1234G").username("tester").email("tester.test.com").providerType(ProviderType.GOOGLE).build();
        userRepository.save(testUser);

        User user = userRepository.findById(1L).orElseThrow(()->new NullPointerException("없음"));

        user.updateMonster(monster);
        monster = monsterRepostory.save(monster);
        user = userRepository.save(user);
        monsterCollection = MonsterCollection.createMonsterCollection(monster);
        monsterCollection = monsterCollectionRepository.save(monsterCollection);
        monsterCollectionDatabase = MonsterCollectionDatabase.from(monsterDatabase4, monsterCollection);
        monsterCollectionDatabaseRepository.save(monsterCollectionDatabase);


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

        //Notice Board
        NoticeVo noticeVo1 =  NoticeVo.builder().title("정식런칭 소개").content("반갑습니다, HabitMonster입니다!\n" +
                "\n" +
                "저희 팀은 많은 분들이 좋은 습관을 생성하고 유지하여 성장하는 습관을 기를 수 \n" +
                "있게 도와주는 서비스를 만들기위해 지난 한달간 달려왔습니다.   \n" +
                "\n" +
                "우리들의 삶은 습관으로 이루어져 있다고 생각합니다. \n" +
                "오늘 하루 우리들은 습관대로 생각하고, 말하고 행동해왔을 겁니다. \n" +
                "이렇게 습관은 항상 우리 곁에 있으며 우리의 정체성을 결정하며 삶의 방향에 영향을 미칩니다.\n" +
                "\n" +
                "HabitMonster와 함께 좋은 습관을 생성하고 유지하는 일상이 하루하루 쌓이다 보면 매일 성장하고 발전하는 여러분들을 보실 수 있을 거라 확신합니다.\n" +
                "\n" +
                "저희팀은 HabitMonster를 사용해 주시는 많은 분들이 좋은 습관을 지속적으로 유지할 수 있게 도와주는 서비스를 만들기 위해 남은 기간 열심히 노력하겠습니다. \n" +
                "\n" +
                "아직 부족한 점이 많겠지만 앞으로 보내주시는 소중한 의견들을 모아 더 나은 서비스를 제공하기 위해 노력하겠습니다.\n" +
                "\n" +
                "HabitMonster 많은 사랑 부탁드리겠습니다! 감사합니다.").build();

        Notice notice = new Notice(noticeVo1);
        noticeRepository.save(notice);

        NoticeVo noticeVo2 =  NoticeVo.builder().title("피드백 이벤트").content("다들 습관 잘 지키고 계신가요?\n" +
                "\n" +
                "저희 팀이 더 나은 서비스를 제공하기 위해 현재 피드백을 받고있지만 피드백 작성이 손이 많이 가는 일인 것 같아요.\n" +
                "\n" +
                "그래서 저희가 작은 이벤트를 준비했습니다." +
                "\n" +
                "피드백을 작성해주신 분들께 추첨을 통해 저희의 마음이 담긴 선물을 드리려고해요!\n" +
                "\n" +
                "참여방법은 아래 글을 참고해주세요.\n" +
                "\n" +
                " 참여방법 첫번째!\n" +
                "1. HabitMonster 페이지에서 서비스 이용 후\n" +
                "메인페이지의 “FeedBack” 아이콘을 누른 후 구글 폼을 사용해 피드백을 제출한다.\n" +
                "2. 0월 0일 추첨을 통해 스타벅스 기프티콘을 받아 맛있게 마신다.\n" +
                "\n" +
                "참여방법 두번째!\n" +
                "1. HabitMonster 페이지에서 서비스 이용 후 인스타그램에 접속한다.\n" +
                "2. HabitMonster를 이용하며 느끼신 장점과 불편한 점을\n" +
                "#HabitMonster #습관 #몬스터  태그와 함께 게시글을 작성한다.\n" +
                "3. 0월 0일 추첨을 통해 베스킨라빈스 파인트 기프티콘을 받아 맛있게 먹는다.").build();

        Notice notice2 = new Notice(noticeVo2);
        noticeRepository.save(notice2);


    }
}
