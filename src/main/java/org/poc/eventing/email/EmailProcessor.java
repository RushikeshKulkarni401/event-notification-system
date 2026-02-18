package org.poc.eventing.email;

import lombok.extern.slf4j.Slf4j;
import org.poc.callback.CallbackService;
import org.poc.configuration.EventConfiguration;
import org.poc.processors.AbstractEventProcessor;
import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailProcessor extends AbstractEventProcessor {

    private final EventConfiguration configuration;

    public EmailProcessor(CallbackService callbackService, EventConfiguration configuration) {
        super(callbackService);
        this.configuration = configuration;
    }

    @Override
    public EventType getSupportedType() {
        return EventType.EMAIL;
    }

    @Override
    protected void process(EventTask task) {

        log.info("Processing EMAIL: {}", task.getEventId());

        simulateDelay(configuration.getDelayForEmail());
        simulateRandomFailure(configuration.getFailureRateForEmail());

        log.info("EMAIL processed successfully: {}", task.getEventId());
    }
}
