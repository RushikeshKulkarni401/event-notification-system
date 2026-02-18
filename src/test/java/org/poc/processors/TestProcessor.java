package org.poc.processors;

import org.poc.callback.CallbackService;
import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;
import org.poc.processors.AbstractEventProcessor;

public class TestProcessor extends AbstractEventProcessor {

    private boolean shouldFail = false;

    public TestProcessor(CallbackService callbackService) {
        super(callbackService);
    }

    @Override
    public EventType getSupportedType() {
        return EventType.EMAIL;
    }

    @Override
    protected void process(EventTask task) {
        if (shouldFail) {
            throw new RuntimeException("Boom");
        }
    }

    public void setShouldFail(boolean value) {
        this.shouldFail = value;
    }
}
