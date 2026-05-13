package com.sritiman.eventreminder.schedulers;

import com.sritiman.eventreminder.entity.Event;
import com.sritiman.eventreminder.schedulers.tasks.EventTask;
import com.sritiman.eventreminder.service.LoaderService;
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
    private final long fixedLoadDelayInSeconds = 60;
    private long loadTimestampEpoch = 0;

    @Scheduled(fixedRate = 1000) //runs every second
    public void runScheduledNotifier() {
        // 5 seconds
        loadTimeElapsedInSeconds ++;

        if(loadTimeElapsedInSeconds == fixedLoadDelayInSeconds || initialDataLoad) {
            //load new events from db
            //populate eventWheel
            populateEventWheel();

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

    private void populateEventWheel() {
        log.atInfo().log("Polling for events");
        loadTimestampEpoch = getCurrentEpochSecond();

        List<Event> events = loaderService.getEventsToBeRemindedTill(
                Instant.ofEpochSecond(loadTimestampEpoch),
                fixedLoadDelayInSeconds
        );

        eventWheel.clear();

        for(Event event : events) {
            long eventReminderEpoch = event.getReminderTime()
                    .getEpochSecond();

            if(loadTimestampEpoch > eventReminderEpoch) {
                continue;
            }

            long tickInstant = (eventReminderEpoch - loadTimestampEpoch) % 60;
            long rounds = Math.floorDiv((eventReminderEpoch - loadTimestampEpoch) , 60);

            addEventToWheel(tickInstant, rounds, event);
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

    /*
    To check if a given instant falls in the time range of currently active wheel
     */
    private boolean isInCurrentWheelRange(Instant time) {
        return time.getEpochSecond() > (loadTimestampEpoch + loadTimeElapsedInSeconds) &&
                time.getEpochSecond() <(loadTimestampEpoch + fixedLoadDelayInSeconds);
    }

    /*
    Adds a new event to the actively running wheel
     */
    public void addEventToActiveWheelViaHotPath(Event event) {
        if(!isInCurrentWheelRange(event.getReminderTime())) {
            log.atInfo().log("{}: This Event is outside active wheel range. Hence " +
                    "it wont be added in hot-path.", event.getEventId());
            return;
        }
        log.atInfo().log("{}: Adding event to current wheel via hotpath.", event.getEventId());

        long eventReminderEpoch = event.getReminderTime().getEpochSecond();
        long currentTime = getCurrentEpochSecond();
        long tickInstant = (eventReminderEpoch - currentTime) % 60;
        long rounds = Math.floorDiv(eventReminderEpoch - currentTime, 60);

        addEventToWheel(tickInstant, rounds, event);
    }

    private void addEventToWheel(long tickInstant, long rounds, Event event) {
        ArrayList<EventTask> eventsAtGivenTick = eventWheel.getOrDefault(tickInstant, new ArrayList<>());
        eventsAtGivenTick.add(
                new EventTask(rounds, event)
        );
        eventWheel.put(tickInstant, eventsAtGivenTick);
    }
}
