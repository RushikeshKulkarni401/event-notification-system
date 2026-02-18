package org.poc.eventing.push;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poc.callback.CallbackService;
import org.poc.configuration.EventConfiguration;
import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PushProcessorTest {
    @Mock
    private CallbackService callbackService;

    @Mock
    private EventConfiguration configuration;

    @Spy
    @InjectMocks
    private PushProcessor pushProcessor;

    private EventTask task;

    @BeforeEach
    void setUp() {
        task = new EventTask();
        task.setEventId("e123");

    }

    @Test
    void shouldReturnEmailAsSupportedType() {
        assertEquals(EventType.PUSH, pushProcessor.getSupportedType());
    }

    @Test
    void process_shouldCallDelayAndFailureSimulation() {
        doNothing().when(pushProcessor).simulateDelay(anyLong());
        doNothing().when(pushProcessor).simulateRandomFailure(anyDouble());

        when(configuration.getDelayForPush()).thenReturn(100L);
        when(configuration.getFailureRateForPush()).thenReturn(0.0);

        pushProcessor.process(task);

        verify(pushProcessor).simulateDelay(100L);
        verify(pushProcessor).simulateRandomFailure(0.0);
    }

    @Test
    void process_shouldThrowWhenFailureOccurs() {
        doNothing().when(pushProcessor).simulateDelay(anyLong());
        doThrow(new RuntimeException("Simulated failure"))
                .when(pushProcessor)
                .simulateRandomFailure(anyDouble());

        when(configuration.getDelayForPush()).thenReturn(100L);
        when(configuration.getFailureRateForPush()).thenReturn(0.0);

        try {
            pushProcessor.process(task);
        } catch (RuntimeException e) {
            assertEquals("Simulated failure", e.getMessage());
        }

        verify(pushProcessor).simulateDelay(100L);
        verify(pushProcessor).simulateRandomFailure(0.0);
    }
}