package org.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailProcessor extends AbstractEventProcessor {

    public EmailProcessor(CallbackService callbackService) {
        super(callbackService);
    }

    @Override
    public EventType getSupportedType() {
        return EventType.EMAIL;
    }

    @Override
    protected void process(EventTask task) {

        log.info("Processing EMAIL: {}", task.getEventId());

        simulateDelay(5);
        simulateRandomFailure(0.1);

        log.info("EMAIL processed successfully: {}", task.getEventId());
    }
}
