package com.sollertia.habit.domain.preset.entity;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.dto.PreSetDto;
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

    public PreSet(PreSetDto preSetDto){
        setCategoryId(preSetDto.getCategoryId());
        setCategory(Category.getCategory(preSetDto.getCategoryId()));
        setCount(preSetDto.getCount());
        setTitle(preSetDto.getTitle());
        setDescription(preSetDto.getDescription());
        setPeriod(preSetDto.getPeriod());
        setPracticeDays(preSetDto.getPracticeDays());
    }

}
