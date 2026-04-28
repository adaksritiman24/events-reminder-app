package com.sritiman.eventreminder.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class EventRequest {
    String eventId;
    String eventName;
    String eventTime;
    Long reminderDurationInSecs;
}
