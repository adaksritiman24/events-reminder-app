package com.sritiman.eventreminder.schedulers;

import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.schedulers.tasks.EventTask;
import com.sritiman.eventreminder.service.LoaderService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimerWheel {

    private final LoaderService loaderService;
    private final HashMap<Long, List<EventTask>> eventWheel = new HashMap<>();
    private long loadTimeElapsedInSeconds = 0;

    @Scheduled(fixedRate = 1000) //runs every second
    public void runScheduledNotifier() {
        // 1hr
        long loadEventsDelayInSeconds = 5;
        loadTimeElapsedInSeconds ++;
        log.atInfo().log("load time: {}", loadTimeElapsedInSeconds);
        if(loadTimeElapsedInSeconds == loadEventsDelayInSeconds) {
            //load new events from db
            //populate eventWheel
            List<Event> events = loaderService.getEventsToBeRemindedTill(loadEventsDelayInSeconds);
            log.atInfo().log("Loaded {} events.", events.size());
            loadTimeElapsedInSeconds = 0;
        }

        long currentEpochInSecond = getCurrentEpochSecond();

        long tickInstant = currentEpochInSecond % 60;

        for(EventTask eventTask : eventWheel.getOrDefault(tickInstant, List.of())) {
            if(eventTask.getRounds() == 0) {
                eventTask.run();
            }
            else {
                eventTask.setRounds(eventTask.getRounds() - 1);
            }
        }
    }

    private long getCurrentEpochSecond() {
        return LocalDateTime
                .now()
                .atZone(ZoneId.of("Asia/Kolkata"))
                .toInstant()
                .getEpochSecond();
    }
}
