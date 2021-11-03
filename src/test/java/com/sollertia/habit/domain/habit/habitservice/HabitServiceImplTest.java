package com.sollertia.habit.domain.habit.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.Repository.HabitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

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