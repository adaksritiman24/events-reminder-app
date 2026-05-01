package com.sritiman.eventreminder.repository;

import com.sritiman.eventreminder.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    @Query("SELECT e FROM Event e WHERE e.reminderTime < :remindersBy")
    public List<Event> getEventsRemindersBy(Instant remindersBy);
}
