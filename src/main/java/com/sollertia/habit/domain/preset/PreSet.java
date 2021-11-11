package com.sollertia.habit.domain.preset;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class PreSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long categoryId;
    private String title;
    private String description;
    private int period;
    private int count;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String practiceDays;

    public PreSet() {

    }

    private void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private void setPeriod(int period) {
        this.period = period;
    }

    private void setCount(int count) {
        this.count = count;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    private void setPracticeDays(String practiceDays) {
        this.practiceDays = practiceDays;
    }

    public PreSet(PreSetVo preSetVo){
        setCategoryId(preSetVo.getCategoryId());
        setCategory(Category.getCategory(preSetVo.getCategoryId()));
        setCount(preSetVo.getCount());
        setTitle(preSetVo.getTitle());
        setDescription(preSetVo.getDescription());
        setPeriod(preSetVo.getPeriod());
        setPracticeDays(preSetVo.getPracticeDays());
    }

}
