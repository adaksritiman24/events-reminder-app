package com.sritiman.eventreminder.schedulers;

import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.schedulers.tasks.EventTask;
import com.sritiman.eventreminder.service.LoaderService;
import com.sritiman.eventreminder.ui.DesktopNotifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimerWheel {

    private final LoaderService loaderService;
    private final HashMap<Long, ArrayList<EventTask>> eventWheel = new HashMap<>();
    private long loadTimeElapsedInSeconds = 0;
    private long tickInstantStart = 0;
    private boolean initialDataLoad = true;

    @Scheduled(fixedRate = 1000) //runs every second
    public void runScheduledNotifier() {
        // 5 seconds
        long loadEventsDelayInSeconds = 3600;
        loadTimeElapsedInSeconds ++;

        if(loadTimeElapsedInSeconds == loadEventsDelayInSeconds || initialDataLoad) {
            //load new events from db
            //populate eventWheel
            populateEventWheel(loadEventsDelayInSeconds);

            //Reset load time elapsed
            loadTimeElapsedInSeconds = 0;
        }
        long tickInstant = tickInstantStart++ % 60;

        for(EventTask eventTask : eventWheel.getOrDefault(tickInstant, new ArrayList<>())) {
            if(eventTask.getRounds() == 0) {
                eventTask.run();
            }
            eventTask.setRounds(eventTask.getRounds() - 1);
        }
    }

    private void populateEventWheel(long loadEventsDelayInSeconds) {
        log.atInfo().log("Polling for events");
        long loadTimestampEpoch = getCurrentEpochSecond();

        List<Event> events = loaderService.getEventsToBeRemindedTill(loadEventsDelayInSeconds);

        eventWheel.clear();

        for(Event event : events) {
            long eventReminderEpoch = event.getReminderTime()
                    .getEpochSecond();

            if(loadTimestampEpoch > eventReminderEpoch) {
                continue;
            }
            long tickInstant = (eventReminderEpoch - loadTimestampEpoch) % 60;
            long rounds = Math.floorDiv((eventReminderEpoch - loadTimestampEpoch) , 60);


            ArrayList<EventTask> eventsAtGivenTick = eventWheel.getOrDefault(tickInstant, new ArrayList<>());
            eventsAtGivenTick.add(
                    new EventTask(rounds, event)
            );
            eventWheel.put(tickInstant, eventsAtGivenTick);
            resetTickInstantStart();

        }
        initialDataLoad = false;
    }

    private long getCurrentEpochSecond() {
        return Instant.now().getEpochSecond();
    }

    private void resetTickInstantStart() {
        this.tickInstantStart = 0;
    }
}
