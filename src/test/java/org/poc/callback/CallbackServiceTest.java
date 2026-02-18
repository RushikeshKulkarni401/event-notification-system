package org.poc.callback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.poc.eventing.model.EventType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallbackServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CallbackService callbackService;

    private CallbackRequest request;

    @BeforeEach
    void setUp() {
        request = new CallbackRequest(
                "e123",
                "COMPLETED",
                EventType.EMAIL,
                null,
                "2025-07-01T12:34:56Z"
        );
    }

    @Test
    void sendCallback_success() {

        when(restTemplate.postForEntity(
                eq("http://localhost:8080/callback"),
                any(CallbackRequest.class),
                eq(Void.class)
        )).thenReturn(ResponseEntity.ok().build());

        callbackService.sendCallback("http://localhost:8080/callback", request);

        verify(restTemplate, times(1))
                .postForEntity("http://localhost:8080/callback", request, Void.class);
    }

    @Test
    void sendCallback_whenException_shouldNotThrow() {
        when(restTemplate.postForEntity(
                anyString(),
                any(CallbackRequest.class),
                eq(Void.class)
        )).thenThrow(new RuntimeException("Connection failed"));

        callbackService.sendCallback("http://localhost:8080/callback", request);

        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(CallbackRequest.class), eq(Void.class));
    }
}
