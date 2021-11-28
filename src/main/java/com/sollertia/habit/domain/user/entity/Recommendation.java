package com.sollertia.habit.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecommendationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    private void setNumber(Integer number) {
        this.number = number;
    }

    private void setType(RecommendationType type) {
        this.type = type;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private static Recommendation of(User user, RecommendationType type) {
        Recommendation recommendation = new Recommendation();
        recommendation.setUser(user);
        recommendation.setType(type);
        return recommendation;
    }

    public static List<Recommendation> listOf(List<User> userList, RecommendationType type) {
        List<Recommendation> recommendationList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            Recommendation recommendation = Recommendation.of(userList.get(i), type);
            recommendation.setNumber(i);
            recommendationList.add(recommendation);
        }
        return recommendationList;
    }
}
