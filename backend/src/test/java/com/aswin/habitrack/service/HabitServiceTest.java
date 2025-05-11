package com.aswin.habitrack.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aswin.habitrack.model.Habit;
import com.aswin.habitrack.model.User;
import com.aswin.habitrack.repository.HabitRepository;
import com.aswin.habitrack.repository.UserRepository;

public class HabitServiceTest {
    private HabitRepository habitRepo;
    private UserRepository userRepo;
    private HabitService habitService;

    @BeforeEach
    void setup() {
        habitRepo = mock(HabitRepository.class);
        userRepo = mock(UserRepository.class);
        habitService = new HabitService(habitRepo, userRepo);
    }

    @Test
    void getHabitsForUser_ReturnsUserHabits() {
        User user = new User();
        user.setUsername("test_user");

        Habit habit = new Habit();
        habit.setName("workout");
        habit.setDescription("damn");

        when(userRepo.findByUsername("test_user")).thenReturn(Optional.of(user));
        when(habitRepo.findByUser(user)).thenReturn(List.of(habit));

        List<Habit> habits = habitService.getHabitsForUser("test_user");

        assertEquals(1, habits.size());
        assertEquals("workout", habits.get(0).getName());
    }

    @Test
    void createHabitForUser_SavesHabitWithUser() {
        User user = new User();
        user.setUsername("test");

        Habit habit = new Habit();
        habit.setName("Exercise");

        when(userRepo.findByUsername("test")).thenReturn(Optional.of(user));
        when(habitRepo.save(any(Habit.class))).thenAnswer(i -> i.getArgument(0));

        Habit saved = habitService.createHabitForUser(habit, "test");

        assertEquals("Exercise", saved.getName());
        assertEquals(user, saved.getUser());
    }

    @Test
    void deleteHabit_DeletesUserHabit() {
        User user = new User();
        user.setUsername("test");

        Habit habit = new Habit();
        habit.setId(1L);
        habit.setUser(user);

        when(habitRepo.findById(1L)).thenReturn(Optional.of(habit));

        habitService.deleteHabit(1L, "test");

        verify(habitRepo).delete(habit);
    }

    @Test
    void deleteHabit_OtherUserHabit_ThrowsException() {
        User user = new User();
        user.setUsername("other");

        Habit habit = new Habit();
        habit.setId(1L);
        habit.setUser(user);

        when(habitRepo.findById(1L)).thenReturn(Optional.of(habit));

        Exception ex = assertThrows(RuntimeException.class, () -> {
            habitService.deleteHabit(1L, "test");
        });

        assertTrue(ex.getMessage().contains("only delete your own"));
    }
}
