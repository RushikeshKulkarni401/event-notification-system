package org.poc;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService {

    private final List<EventProcessor> processors;

    private final Map<EventType, EventProcessor> processorMap =
            new EnumMap<>(EventType.class);

    @PostConstruct
    public void init() {
        for (EventProcessor processor : processors) {
            processorMap.put(processor.getSupportedType(), processor);
        }
    }

    public String submitEvent(EventRequest request) {

        String eventId = UUID.randomUUID().toString();

        EventTask task = new EventTask(
                eventId,
                request,
                Instant.now()
        );

        EventProcessor processor =
                processorMap.get(request.getEventType());

        if (processor == null) {
            throw new IllegalArgumentException("Unsupported event type");
        }

        processor.submit(task);

        return eventId;
    }
}
