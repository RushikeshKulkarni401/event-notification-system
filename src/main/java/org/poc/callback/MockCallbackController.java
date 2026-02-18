package org.poc.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mock-client")
@Slf4j
public class MockCallbackController {

    @PostMapping("/event-status")
    public ResponseEntity<String> receiveCallback(
            @RequestBody CallbackRequest request) {

        // basic logging to check parameters for callback
        log.info("Received callback:");
        log.info("EventId: {}", request.getEventId());
        log.info("Status: {}", request.getStatus());
        log.info("EventType: {}", request.getEventType());
        log.info("Error: {}", request.getErrorMessage());
        log.info("ProcessedAt: {}", request.getProcessedAt());

        return ResponseEntity.ok("Callback received successfully");
    }
}
