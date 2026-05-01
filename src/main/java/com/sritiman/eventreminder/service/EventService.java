package com.sritiman.eventreminder.service;

import com.sritiman.eventreminder.dto.request.EventRequest;
import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class EventService {

    EventRepository eventRepository;

    public String addEvent(EventRequest eventRequest) {
        Instant eventTime = getLocalDateTimeInstant(eventRequest.getEventTime());
        Event event = Event.builder()
                .eventId(eventRequest.getEventId())
                .eventTime(eventTime)
                .eventName(eventRequest.getEventName())
                .reminderTime(
                        getReminderTime(
                        eventTime,
                        eventRequest.getReminderDurationInSecs()
                        )
                )
                .build();

        return eventRepository
                .save(event)
                .getEventId();
    }

    private Instant getLocalDateTimeInstant(String input) {
        LocalDateTime ldt = LocalDateTime.parse(
                input,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("Asia/Kolkata"));

        return zdt.toInstant();
    }

    private Instant getReminderTime(Instant eventTime, long reminderInSeconds) {
        return eventTime.minus(reminderInSeconds, ChronoUnit.SECONDS);
    }
}
