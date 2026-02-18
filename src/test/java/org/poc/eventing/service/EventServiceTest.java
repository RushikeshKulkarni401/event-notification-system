package org.poc.eventing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poc.eventing.model.EventRequest;
import org.poc.eventing.model.EventTask;
import org.poc.eventing.model.EventType;
import org.poc.exception.EventingException;
import org.poc.processors.EventProcessor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventProcessor emailProcessor;

    @Mock
    private EventProcessor smsProcessor;

    private EventService eventService;

    @BeforeEach
    void setUp() {
        when(emailProcessor.getSupportedType()).thenReturn(EventType.EMAIL);
        when(smsProcessor.getSupportedType()).thenReturn(EventType.SMS);

        eventService = new EventService(List.of(emailProcessor, smsProcessor));
        eventService.init(); // manually call PostConstruct
    }

    @Test
    void submitEvent_shouldRouteToCorrectProcessor() throws EventingException {
        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);

        String eventId = eventService.submitEvent(request);

        assertNotNull(eventId);
        verify(emailProcessor, times(1)).submit(any(EventTask.class));
        verify(smsProcessor, never()).submit(any());
    }

    @Test
    void submitEvent_shouldThrowWhenUnsupportedType() {
        EventRequest request = new EventRequest();
        request.setEventType(EventType.PUSH); // not registered

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> eventService.submitEvent(request)
        );

        assertEquals("Unsupported event type", exception.getMessage());
    }

    @Test
    void init_shouldRegisterAllProcessors() {
        assertDoesNotThrow(() -> eventService.submitEvent(
                new EventRequest() {{
                    setEventType(EventType.EMAIL);
                }}
        ));
    }
}
