package com.sollertia.habit.domain.preset.enums;

import com.sollertia.habit.domain.preset.dto.PreSetVo;

import java.util.ArrayList;
import java.util.List;

public enum PreSet {

    Water(makePreSet("water")), WorkOut(makePreSet("workOut")),
    Laugh(makePreSet("laugh")), Meditation(makePreSet("meditation"));
//    Water(makePreSet("water")), WorkOut(makePreSet("workOut")),
//    Water(makePreSet("water")), WorkOut(makePreSet("workOut")),
//    Water(makePreSet("water")), WorkOut(makePreSet("workOut")),
//    Water(makePreSet("water")), WorkOut(makePreSet("workOut")),
//    Water(makePreSet("water")), WorkOut(makePreSet("workOut"));

    private final PreSetVo preset;

    PreSet(PreSetVo preset) {
        this.preset = preset;
    }

    public PreSetVo getPreSet() {
        return this.preset;
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

    private static List<Integer> makeWeek(int num) {
        List<Integer> week = new ArrayList<>();
        switch (num) {
            case 1:
                week.add(2);
                week.add(4);
                week.add(6); // 월, 수, 금
                break;
            case 2:
                week.add(3);
                week.add(5); // 화, 목
                break;
            case 3:
                week.add(1);
                week.add(7); // 일, 토
                break;
        }
        return week;
    }


    private static PreSetVo makePreSet(String title) {
        // ================================Health================================
        // Water
        PreSetVo water = PreSetVo.builder().presetId(1L).category(Category.Health).categoryId(Category.Health.getType()).title("물 3잔 마시기").description("한잔에 300ml 이상")
                .count(3L).period(30L).session("nPerDay").build();
        // WorkOut
        PreSetVo workOut = PreSetVo.builder().presetId(2L).category(Category.Health).categoryId(Category.Health.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();

        // ================================Emotion================================
        // WorkOut
        PreSetVo laugh = PreSetVo.builder().presetId(3L).category(Category.Emotion).categoryId(Category.Emotion.getType()).title("하루에 3번 웃기").description("아주 크게 웃기")
                .count(3L).period(30L).session("nPerDay").build();
        // WorkOu
        PreSetVo meditation = PreSetVo.builder().presetId(4L).category(Category.Emotion).categoryId(Category.Emotion.getType()).title("명상하기").description("토, 일")
                .count(1L).period(30L).session("specificDay").week(makeWeek(3)).build();

        // ================================Hobby================================
        // WorkOut
        PreSetVo a = PreSetVo.builder().presetId(5L).category(Category.Hobby).categoryId(Category.Hobby.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();
        // WorkOut
        PreSetVo b = PreSetVo.builder().presetId(6L).category(Category.Hobby).categoryId(Category.Hobby.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();

        // ================================Life================================
        // WorkOut
        PreSetVo c = PreSetVo.builder().presetId(7L).category(Category.Life).categoryId(Category.Life.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();
        // WorkOut
        PreSetVo d = PreSetVo.builder().presetId(8L).category(Category.Life).categoryId(Category.Life.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();

        // ================================Study================================
        // WorkOut
        PreSetVo e = PreSetVo.builder().presetId(9L).category(Category.Study).categoryId(Category.Study.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();
        // WorkOut
        PreSetVo f = PreSetVo.builder().presetId(10L).category(Category.Study).categoryId(Category.Study.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();

        // ================================Relation================================
        // WorkOut
        PreSetVo g = PreSetVo.builder().presetId(11L).category(Category.Relation).categoryId(Category.Relation.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();
        // WorkOut
        PreSetVo h = PreSetVo.builder().presetId(12L).category(Category.Relation).categoryId(Category.Relation.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();

        // ================================Etc================================
        // WorkOut
        PreSetVo i = PreSetVo.builder().presetId(13L).category(Category.Etc).categoryId(Category.Etc.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();
        // WorkOut
        PreSetVo o = PreSetVo.builder().presetId(14L).category(Category.Etc).categoryId(Category.Etc.getType()).title("헬스장 2시간 운동").description("월, 수, 금")
                .count(1L).period(30L).session("specificDay").week(makeWeek(1)).build();

        switch (title) {
            case "water":
                return water;
            case "workOut":
                return workOut;
            case "laugh":
                return laugh;
            case "meditation":
                return meditation;
            default:
                return null;
        }
    }
}
