package com.sritiman.eventreminder.service;

import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class LoaderService {

    private EventRepository eventRepository;

    public List<Event> getEventsToBeRemindedTill(Instant startTime, long timeInSeconds) {

        Instant remindersBy = startTime
                .plusSeconds(timeInSeconds);

        return eventRepository.getEventsRemindersBy(startTime, remindersBy);
    }
}
