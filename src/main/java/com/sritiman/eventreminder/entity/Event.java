package com.sritiman.eventreminder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
    Long reminderDurationInSecs;
}
