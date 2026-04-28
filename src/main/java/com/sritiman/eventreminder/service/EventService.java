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

@Service
@AllArgsConstructor
public class EventService {

    EventRepository eventRepository;

    public String addEvent(EventRequest eventRequest) {
        Event event = Event.builder()
                .eventId(eventRequest.getEventId())
                .eventTime(getLocalDateTimeInstant(eventRequest.getEventTime()))
                .eventName(eventRequest.getEventName())
                .reminderDurationInSecs(eventRequest.getReminderDurationInSecs())
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
}
