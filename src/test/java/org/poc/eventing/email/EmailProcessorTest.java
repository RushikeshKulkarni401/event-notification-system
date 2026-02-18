package org.poc.eventing.email;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailProcessorTest {

    @Mock
    private CallbackService callbackService;

    @Mock
    private EventConfiguration configuration;

    @Spy
    @InjectMocks
    private EmailProcessor emailProcessor;

    private EventTask task;

    @BeforeEach
    void setUp() {
        task = new EventTask();
        task.setEventId("e123");

    }

    @Test
    void shouldReturnEmailAsSupportedType() {
        assertEquals(EventType.EMAIL, emailProcessor.getSupportedType());
    }

    @Test
    void process_shouldCallDelayAndFailureSimulation() {
        // Prevent actual delay or randomness
        doNothing().when(emailProcessor).simulateDelay(anyLong());
        doNothing().when(emailProcessor).simulateRandomFailure(anyDouble());

        when(configuration.getDelayForEmail()).thenReturn(100L);
        when(configuration.getFailureRateForEmail()).thenReturn(0.0);

        emailProcessor.process(task);

        verify(emailProcessor).simulateDelay(100L);
        verify(emailProcessor).simulateRandomFailure(0.0);
    }

    @Test
    void process_shouldThrowWhenFailureOccurs() {
        doNothing().when(emailProcessor).simulateDelay(anyLong());
        doThrow(new RuntimeException("Simulated failure"))
                .when(emailProcessor)
                .simulateRandomFailure(anyDouble());

        when(configuration.getDelayForEmail()).thenReturn(100L);
        when(configuration.getFailureRateForEmail()).thenReturn(0.0);

        try {
            emailProcessor.process(task);
        } catch (RuntimeException e) {
            assertEquals("Simulated failure", e.getMessage());
        }

        verify(emailProcessor).simulateDelay(100L);
        verify(emailProcessor).simulateRandomFailure(0.0);
    }
}
