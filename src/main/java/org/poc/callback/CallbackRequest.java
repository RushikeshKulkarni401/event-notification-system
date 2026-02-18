package org.poc.callback;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poc.eventing.model.EventType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallbackRequest {

    private String eventId;
    private String status;        // COMPLETED or FAILED
    private EventType eventType;
    private String errorMessage;  // null for success
    private String processedAt;   // ISO-8601 timestamp
}
