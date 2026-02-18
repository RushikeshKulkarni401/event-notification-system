package org.poc;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class EventTask {
    private String eventId;
    private EventRequest request;
    private Instant createdAt;

    EventTask(String eventId, EventRequest request, Instant createdAt) {
        this.eventId = eventId;
        this.request = request;
        this.createdAt = createdAt;
    }
}
