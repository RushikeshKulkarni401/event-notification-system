package org.poc.eventing.push;

import lombok.extern.slf4j.Slf4j;
import org.poc.callback.CallbackService;
import org.poc.configuration.EventConfiguration;
import org.poc.processors.AbstractEventProcessor;
import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushProcessor extends AbstractEventProcessor {

    private final EventConfiguration configuration;

    public PushProcessor(CallbackService callbackService, EventConfiguration configuration) {
        super(callbackService);
        this.configuration = configuration;
    }

    @Override
    public EventType getSupportedType() {
        return EventType.PUSH;
    }

    @Override
    protected void process(EventTask task) {

        log.info("Processing PUSH: {}", task.getEventId());

        simulateDelay(configuration.getDelayForPush());
        simulateRandomFailure(configuration.getFailureRateForPush());

        log.info("PUSH processed successfully: {}", task.getEventId());
    }
}
