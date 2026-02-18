package org.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsProcessor extends AbstractEventProcessor {

    public SmsProcessor(CallbackService callbackService) {
        super(callbackService);
    }

    @Override
    public EventType getSupportedType() {
        return EventType.SMS;
    }

    @Override
    protected void process(EventTask task) {

        log.info("Processing SMS: {}", task.getEventId());

        simulateDelay(3);
        simulateRandomFailure(0.1);

        log.info("SMS processed successfully: {}", task.getEventId());
    }
}
