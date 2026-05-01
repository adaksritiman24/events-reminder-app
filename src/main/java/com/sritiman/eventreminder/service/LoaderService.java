package com.sritiman.eventreminder.service;

import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class LoaderService {

    private EventRepository eventRepository;

    public List<Event> getEventsToBeRemindedTill(long timeInSeconds) {
        Instant remindersBy = LocalDateTime
                .now()
                .atZone(ZoneId.of("Asia/Kolkata"))
                .plusSeconds(timeInSeconds)
                .toInstant();

        return eventRepository.getEventsRemindersBy(remindersBy);
    }
}
