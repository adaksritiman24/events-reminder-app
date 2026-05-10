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
        Instant currentTime = Instant.now();
        Instant remindersBy = currentTime
                .plusSeconds(timeInSeconds);

        return eventRepository.getEventsRemindersBy(currentTime, remindersBy);
    }
}
