package org.poc.processors;

import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;
import org.poc.exception.EventingException;

public interface EventProcessor {

    EventType getSupportedType();

    void submit(EventTask task) throws EventingException;

    void shutdown();
}
