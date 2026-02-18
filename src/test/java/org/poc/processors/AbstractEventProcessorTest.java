package org.poc.processors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poc.callback.CallbackService;
import org.poc.eventing.model.EventRequest;
import org.poc.eventing.model.EventTask;
import org.poc.exception.EventingException;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractEventProcessorTest {

    @Mock
    private CallbackService callbackService;

    private TestProcessor processor;

    @BeforeEach
    void setup() {
        processor = new TestProcessor(callbackService);
        processor.start(); // start worker thread
    }

    @Test
    void submit_shouldQueueEvent_whenNotPaused() throws Exception {
        EventRequest request = new EventRequest();
        request.setCallbackUrl("http://localhost/test");

        EventTask task = new EventTask("e1", request, Instant.now());

        processor.submit(task);

        Thread.sleep(500); // allow worker to process

        verify(callbackService, atLeastOnce())
                .sendCallback(eq("http://localhost/test"), any());
    }

    @Test
    void submit_shouldThrow_whenPaused() {
        processor.toggle(); // pause

        EventTask task = mock(EventTask.class);

        assertThrows(EventingException.class,
                () -> processor.submit(task));
    }

    @Test
    void shouldSendFailureCallback_whenProcessThrows() throws Exception {
        processor.setShouldFail(true);

        EventRequest request = new EventRequest();
        request.setCallbackUrl("http://localhost/test");

        EventTask task = new EventTask("e2", request, Instant.now());

        processor.submit(task);

        Thread.sleep(500);

        verify(callbackService)
                .sendCallback(eq("http://localhost/test"),
                        argThat(req -> req.getStatus().equals("FAILED")));
    }
}
