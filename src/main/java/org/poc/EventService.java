package org.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventService {

    public String submitEvent(EventRequest request) {

        String eventId = UUID.randomUUID().toString();

        log.info("Received event: eventId={}, type={}, callbackUrl={}",
                eventId,
                request.getEventType(),
                request.getCallbackUrl());

        EventType type = request.getEventType();
        EventPayload payload = request.getPayload();

        if (EventType.EMAIL.equals(type)) {
            log.info("Email event found");
        }
        else if (EventType.SMS.equals(type)) {
            log.info("SMS event found");
        }
        else if (EventType.PUSH.equals(type)) {
            log.info("Push event found");
        }
        else {
            throw new IllegalArgumentException("Unsupported payload type");
        }
        return eventId;
    }
}

