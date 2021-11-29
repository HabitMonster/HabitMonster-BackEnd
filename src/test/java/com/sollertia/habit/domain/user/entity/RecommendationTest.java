package com.sollertia.habit.domain.user.entity;

import com.sollertia.habit.domain.user.enums.RecommendationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class RecommendationTest {

    Recommendation recommendation;
    User user;

    @BeforeEach
    void setUp() {
        recommendation = new Recommendation();
        user = mock(User.class);
        Whitebox.setInternalState(recommendation, "id", 1L);
    }

    @Test
    void listOf() {
        List<Recommendation> recommendationList = Recommendation.listOf(List.of(user), RecommendationType.CREATE_TOP10);
        assertThat(recommendationList.get(0).getNumber()).isEqualTo(0);
        assertThat(recommendationList.get(0).getType()).isEqualTo(RecommendationType.CREATE_TOP10);
        assertThat(recommendationList.get(0).getUser()).isEqualTo(user);
    }

    @Test
    void getId() {
        assertThat(recommendation.getId()).isEqualTo(1L);
    }
}