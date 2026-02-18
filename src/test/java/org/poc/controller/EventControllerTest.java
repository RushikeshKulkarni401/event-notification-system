package org.poc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.poc.eventing.model.EventRequest;
import org.poc.eventing.model.EventType;
import org.poc.eventing.service.EventService;
import org.poc.exception.EventingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEvent_shouldReturn200_whenSuccess() throws Exception {

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);

        Mockito.when(eventService.submitEvent(any()))
                .thenReturn("123");

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId", is("123")))
                .andExpect(jsonPath("$.message",
                        is("Event accepted for processing.")));
    }

    @Test
    void createEvent_shouldReturn503_whenEventingException() throws Exception {

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);

        Mockito.when(eventService.submitEvent(any()))
                .thenThrow(new EventingException("System paused"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.message", is("System paused")));
    }

    @Test
    void createEvent_shouldReturn500_whenUnexpectedException() throws Exception {

        EventRequest request = new EventRequest();
        request.setEventType(EventType.EMAIL);

        Mockito.when(eventService.submitEvent(any()))
                .thenThrow(new RuntimeException("Something broke"));

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Something broke")));
    }
}
