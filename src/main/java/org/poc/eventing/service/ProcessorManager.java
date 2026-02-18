package org.poc.eventing.service;

import org.poc.processors.AbstractEventProcessor;
import org.poc.processors.EventProcessor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProcessorManager {

    private final List<EventProcessor> processors;

    public ProcessorManager(List<EventProcessor> processors) {
        this.processors = processors;
    }

    // toggle all the processors
    public void toggleAll() {
        processors.forEach(p -> {
            if (p instanceof AbstractEventProcessor processor) {
                processor.toggle();
            }
        });
    }

    public Map<String, Boolean> getStatus() {
        Map<String, Boolean> status = new HashMap<>();
        processors.forEach(p -> {
            if (p instanceof AbstractEventProcessor processor) {
                status.put(processor.getSupportedType().name(),
                        processor.isPaused());
            }
        });
        return status;
    }
}
