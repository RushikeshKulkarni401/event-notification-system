package org.poc.eventing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poc.eventing.model.EventType;
import org.poc.processors.AbstractEventProcessor;
import org.poc.processors.EventProcessor;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessorManagerTest {

    @Mock
    private AbstractEventProcessor emailProcessor;

    @Mock
    private AbstractEventProcessor smsProcessor;

    @Mock
    private EventProcessor nonAbstractProcessor; // should be ignored

    private ProcessorManager processorManager;

    @BeforeEach
    void setUp() {
        processorManager = new ProcessorManager(
                List.of(emailProcessor, smsProcessor, nonAbstractProcessor)
        );
    }

    @Test
    void toggleAll_shouldCallToggleOnAllAbstractProcessors() {
        processorManager.toggleAll();

        verify(emailProcessor, times(1)).toggle();
        verify(smsProcessor, times(1)).toggle();

        // should not interact with non-abstract processor
        verifyNoInteractions(nonAbstractProcessor);
    }

    @Test
    void getStatus_shouldReturnCorrectStatusMap() {
        when(emailProcessor.getSupportedType()).thenReturn(EventType.EMAIL);
        when(emailProcessor.isPaused()).thenReturn(true);

        when(smsProcessor.getSupportedType()).thenReturn(EventType.SMS);
        when(smsProcessor.isPaused()).thenReturn(false);

        Map<String, Boolean> status = processorManager.getStatus();

        assertEquals(2, status.size());
        assertTrue(status.get("EMAIL"));
        assertFalse(status.get("SMS"));
    }
}
