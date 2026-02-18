package org.poc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<EventResponse> createEvent(
            @RequestBody EventRequest request) {

        try {
            String eventId = eventService.submitEvent(request);
            return new ResponseEntity<>(
                    new EventResponse(eventId, "Event accepted for processing."), HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new EventResponse(request.getEventType().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
