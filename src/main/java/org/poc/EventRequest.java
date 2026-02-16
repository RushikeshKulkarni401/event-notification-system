package org.poc;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequest {
    private EventType eventType;
    private EventPayload payload;
    private String callbackUrl;
}

