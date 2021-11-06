package com.sollertia.habit.domain.habit.service;

import com.sollertia.habit.domain.habit.repository.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HabitServiceImplTest {

    @Mock
    HabitRepository habitRepository;

    @InjectMocks
    HabitServiceImpl habitService;

    @Test
    public void saveNewHabit() {

    }

}