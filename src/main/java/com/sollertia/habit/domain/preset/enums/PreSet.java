package com.sollertia.habit.domain.preset.enums;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.dto.PreSetVo;

import java.util.ArrayList;
import java.util.List;

public enum PreSet {

    Water(makePreSet("water")), WorkOut(makePreSet("workOut")),
    Laugh(makePreSet("laugh")), Meditation(makePreSet("meditation")),
    ReadBook(makePreSet("readBook")), Climbing(makePreSet("climbing")),
    WashingDishes(makePreSet("washingDishes")), Wash(makePreSet("wash")),
    English(makePreSet("english")), Math(makePreSet("math")),
    Friend(makePreSet("friend")), Filial(makePreSet("filial")),
    Lol(makePreSet("lol")), Mung(makePreSet("mung"));

    private final PreSetVo preset;

    PreSet(PreSetVo preset) {
        this.preset = preset;
    }

    public static PreSetVo getPreSet(Long presetId) {
        for (PreSet p : PreSet.values()) {
            if (p.preset.getPresetId().equals(presetId)) {
                return p.preset;
            }
        }
        return null;
    }

    public static List<PreSetVo> getPreSetList(Long categoryId) {
        List<PreSetVo> list = new ArrayList<>();
        for (PreSet p : PreSet.values()) {
            if (p.preset.getCategoryId().equals(categoryId)) {
                list.add(p.preset);
            }
        }
        return list;
    }

    private static String practiceDays(int num) {
        switch (num) {
            case 1:
                return "135";// 월, 수, 금
            case 2:
               return "24"; // 화, 목
            case 3:
               return "06"; // 일, 토
            default: return "0123456";
        }
    }


    private static PreSetVo makePreSet(String title) {
        // ================================Health================================
        // water
        PreSetVo water = PreSetVo.builder().presetId(1L).category(Category.Health).categoryId(Category.Health.getCategoryId()).title("물 3잔 마시기").description("한잔에 300ml 이상")
                .count(3).period(30).practiceDays(practiceDays(0)).build();
        // workOut
        PreSetVo workOut = PreSetVo.builder().presetId(2L).category(Category.Health).categoryId(Category.Health.getCategoryId()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1).period(30).practiceDays(practiceDays(1)).build();

        // ================================Emotion================================
        // laugh
        PreSetVo laugh = PreSetVo.builder().presetId(3L).category(Category.Emotion).categoryId(Category.Emotion.getCategoryId()).title("하루에 3번 웃기").description("아주 크게 웃기")
                .count(3).period(30).practiceDays(practiceDays(0)).build();
        // meditation
        PreSetVo meditation = PreSetVo.builder().presetId(4L).category(Category.Emotion).categoryId(Category.Emotion.getCategoryId()).title("명상하기 30분").description("토, 일")
                .count(1).period(30).practiceDays(practiceDays(3)).build();

        // ================================Hobby================================
        // readBook
        PreSetVo readBook = PreSetVo.builder().presetId(5L).category(Category.Hobby).categoryId(Category.Hobby.getCategoryId()).title("독서 2시간하기").description("매일 잠들기 전")
                .count(1).period(30).practiceDays(practiceDays(0)).build();
        // climbing
        PreSetVo climbing = PreSetVo.builder().presetId(6L).category(Category.Hobby).categoryId(Category.Hobby.getCategoryId()).title("암벽등반 1시간").description("화, 목")
                .count(1).period(30).practiceDays(practiceDays(2)).build();

        // ================================Life================================
        // washingDishes
        PreSetVo washingDishes = PreSetVo.builder().presetId(7L).category(Category.Life).categoryId(Category.Life.getCategoryId()).title("설겆이 2번").description("아침, 저녁")
                .count(2).period(30).practiceDays(practiceDays(0)).build();
        // wash
        PreSetVo wash = PreSetVo.builder().presetId(8L).category(Category.Life).categoryId(Category.Life.getCategoryId()).title("빨래 하기").description("월, 수, 금")
                .count(1).period(30).practiceDays(practiceDays(1)).build();

        // ================================Study================================
        // english
        PreSetVo english = PreSetVo.builder().presetId(9L).category(Category.Study).categoryId(Category.Study.getCategoryId()).title("영어회화 공부 1시간").description("매일 저녁 잠들기 전")
                .count(1).period(30).practiceDays(practiceDays(0)).build();
        // math
        PreSetVo math = PreSetVo.builder().presetId(10L).category(Category.Study).categoryId(Category.Study.getCategoryId()).title("수학 문제집 풀기").description("월, 수, 금")
                .count(1).period(30).practiceDays(practiceDays(1)).build();

        // ================================Relation================================
        // friend
        PreSetVo friend = PreSetVo.builder().presetId(11L).category(Category.Relation).categoryId(Category.Relation.getCategoryId()).title("친구에게 전화하기").description("매일 한명씩 돌아가면서")
                .count(1).period(30).practiceDays(practiceDays(0)).build();
        // filial
        PreSetVo filial = PreSetVo.builder().presetId(12L).category(Category.Relation).categoryId(Category.Relation.getCategoryId()).title("부모님께 사랑한다 말하기 하루에 3번").description("토, 일")
                .count(3).period(30).practiceDays(practiceDays(3)).build();

        // ================================Etc================================
        // WorkOut
        PreSetVo lol = PreSetVo.builder().presetId(13L).category(Category.Etc).categoryId(Category.Etc.getCategoryId()).title("LOL 하기 5시간이상").description("LOL 프로게이머 준비 중")
                .count(1).period(30).practiceDays(practiceDays(0)).build();
        // WorkOut
        PreSetVo mung = PreSetVo.builder().presetId(14L).category(Category.Etc).categoryId(Category.Etc.getCategoryId()).title("멍 때리기 1시간").description("머리를 비워보자 - 화, 목")
                .count(1).period(30).practiceDays(practiceDays(2)).build();

        switch (title) {
            case "water":
                return water;
            case "workOut":
                return workOut;
            case "laugh":
                return laugh;
            case "meditation":
                return meditation;
            case "readBook":
                return readBook;
            case "climbing":
                return climbing;
            case "washingDishes":
                return washingDishes;
            case "wash":
                return wash;
            case "english":
                return english;
            case "math":
                return math;
            case "friend":
                return friend;
            case "filial":
                return filial;
            case "lol":
                return lol;
            case "mung":
                return mung;
            default:
                return null;
        }
    }
}
