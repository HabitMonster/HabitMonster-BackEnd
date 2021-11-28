package com.sollertia.habit.global.scheduler.entity;

import com.sollertia.habit.domain.category.enums.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class CategoryAvg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    Category category;

    Long achievementPercentage;

    public CategoryAvg(Category category, Long achievementPercentage){
        this.category = category;
        this.achievementPercentage = achievementPercentage;
    }

}
