package com.sritiman.eventreminder.controller;

import com.sritiman.eventreminder.dto.request.EventRequest;
import com.sritiman.eventreminder.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EventsController {

    EventService eventService;

    @PostMapping("/events")
    public ResponseEntity<String> addEvent(@RequestBody EventRequest eventRequest) {
        return new ResponseEntity<>(
                eventService.addEvent(eventRequest),
                HttpStatus.CREATED
        );
    }
}
