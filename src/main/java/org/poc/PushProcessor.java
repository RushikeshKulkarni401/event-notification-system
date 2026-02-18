package org.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PushProcessor extends AbstractEventProcessor {


    public PushProcessor(CallbackService callbackService) {
        super(callbackService);
    }

    @Override
    public EventType getSupportedType() {
        return EventType.PUSH;
    }

    @Override
    protected void process(EventTask task) {

        log.info("Processing PUSH: {}", task.getEventId());

        simulateDelay(2);
        simulateRandomFailure(0.1);

        log.info("PUSH processed successfully: {}", task.getEventId());
    }
}
