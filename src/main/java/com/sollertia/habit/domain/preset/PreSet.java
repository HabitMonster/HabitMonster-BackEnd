package com.sollertia.habit.domain.preset;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private Long count;
    private Category category;
    private String practiseDays;


}
