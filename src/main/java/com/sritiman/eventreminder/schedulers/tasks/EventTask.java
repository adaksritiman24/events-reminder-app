package com.sritiman.eventreminder.schedulers.tasks;

import com.sritiman.eventreminder.entity.Event;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
public class EventTask {
    long rounds;
    Event event;

    public void run() {
        log.atInfo().log("{} Event reminder. Event at: {}",
                event.getEventId(), event.getEventTime());
    }
}
