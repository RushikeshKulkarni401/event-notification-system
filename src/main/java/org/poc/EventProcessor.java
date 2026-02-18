package org.poc;

public interface EventProcessor {

    EventType getSupportedType();

    void submit(EventTask task);

    void shutdown();
}
