package com.aswin.habitrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aswin.habitrack.model.Habit;
import com.aswin.habitrack.model.User;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUser(User user);
}
