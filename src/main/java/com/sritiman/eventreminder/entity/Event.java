package com.sritiman.eventreminder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "events")
public class Event {
    @Id
    String eventId;
    String eventName;
    Instant eventTime;
    Instant reminderTime;

    public ZonedDateTime getEventTimeAtLocaleInstant() {
        return eventTime.atZone(ZoneId.of("Asia/Kolkata"));
    }

    public ZonedDateTime getReminderTimeAtLocaleInstant() {
        return reminderTime.atZone(ZoneId.of("Asia/Kolkata"));
    }
}
