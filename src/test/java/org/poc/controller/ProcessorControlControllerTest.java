package org.poc.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.poc.eventing.service.ProcessorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProcessorControlController.class)
class ProcessorControlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessorManager processorManager;

    @Test
    void toggleProcessors_shouldReturn200AndCallService() throws Exception {

        mockMvc.perform(post("/api/processors/toggle"))
                .andExpect(status().isOk())
                .andExpect(content().string("Processors toggled successfully"));

        verify(processorManager).toggleAll();
    }

    @Test
    void status_shouldReturnProcessorStatusMap() throws Exception {

        Map<String, Boolean> statusMap = Map.of(
                "EMAIL", true,
                "SMS", false
        );

        Mockito.when(processorManager.getStatus())
                .thenReturn(statusMap);

        mockMvc.perform(get("/api/processors/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.EMAIL", is(true)))
                .andExpect(jsonPath("$.SMS", is(false)));
    }
}
