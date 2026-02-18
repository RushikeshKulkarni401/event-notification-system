package org.poc.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.poc.eventing.model.EventRequest;
import org.poc.eventing.model.EventResponse;
import org.poc.eventing.service.EventService;
import org.poc.exception.EventingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Event API", description = "APIs for creating notification events")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(
            summary = "Create notification event",
            description = "Creates EMAIL, SMS, or PUSH notification event"
    )
    @ApiResponse(responseCode = "200", description = "Event accepted for processing")
    @PostMapping(produces = "application/json")
    public ResponseEntity<EventResponse> createEvent(
            @Valid @RequestBody EventRequest request) {
        try {
            String eventId = eventService.submitEvent(request);
            return new ResponseEntity<>(
                    new EventResponse(eventId, "Event accepted for processing."), HttpStatus.OK
            );
        } catch (EventingException e) {
            return new ResponseEntity<>(
                    new EventResponse(request.getEventType().toString(), e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new EventResponse(request.getEventType().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
