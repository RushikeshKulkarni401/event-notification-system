package org.poc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CallbackService {

    private final RestTemplate restTemplate;

    public CallbackService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendCallback(String callbackUrl, CallbackRequest request) {
        try {
            restTemplate.postForEntity(callbackUrl, request, Void.class);
            log.info("Callback sent for event {}", request.getEventId());
        } catch (Exception e) {
            log.error("Callback failed for event {}: {}",
                    request.getEventId(),
                    e.getMessage());
        }
    }
}
