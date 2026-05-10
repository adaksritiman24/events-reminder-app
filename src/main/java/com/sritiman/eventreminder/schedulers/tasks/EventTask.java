package com.sritiman.eventreminder.schedulers.tasks;

import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.ui.DesktopNotifier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Data
@Builder
@AllArgsConstructor
public class EventTask {
    long rounds;
    Event event;

    public void run() {
        log.atInfo().log("{} Event reminder. Event at: {}, reminder at: {}",
                event.getEventId(), event.getEventTimeAtLocaleInstant(), event.getReminderTimeAtLocaleInstant());

        DesktopNotifier.showNotification(
                String.format("Event Reminder: %s", event.getEventName()),
                getTimeRemainingMessage(event.getEventTime())
        );
    }

    public String getTimeRemainingMessage(Instant eventTime) {

        Duration duration = Duration.between(
                Instant.now(),
                eventTime
        );

        long seconds = duration.getSeconds();

        if (seconds < 60) {
            return "Starts in " + seconds + " seconds";
        }

        long minutes = seconds / 60;

        if (minutes < 60) {
            return "Starts in " + minutes + " minutes";
        }

        long hours = minutes / 60;

        return "Starts in " + hours + " hours";
    }
}
