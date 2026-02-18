package org.poc.eventing.sms;

import lombok.extern.slf4j.Slf4j;
import org.poc.callback.CallbackService;
import org.poc.configuration.EventConfiguration;
import org.poc.processors.AbstractEventProcessor;
import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsProcessor extends AbstractEventProcessor {

    private final EventConfiguration configuration;
    public SmsProcessor(CallbackService callbackService, EventConfiguration configuration) {
        super(callbackService);
        this.configuration = configuration;
    }

    @Override
    public EventType getSupportedType() {
        return EventType.SMS;
    }

    @Override
    protected void process(EventTask task) {

        log.info("Processing SMS: {}", task.getEventId());

        simulateDelay(configuration.getDelayForSms());
        simulateRandomFailure(configuration.getFailureRateForSms());

        log.info("SMS processed successfully: {}", task.getEventId());
    }
}
