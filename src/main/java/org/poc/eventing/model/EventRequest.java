package org.poc.eventing.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.poc.eventing.email.EmailPayload;
import org.poc.eventing.push.PushPayload;
import org.poc.eventing.sms.SmsPayload;

@Getter
@Setter
@Schema(description = "Event creation request")
public class EventRequest {
    @Schema(description = "Type of event", example = "EMAIL")
    private EventType eventType;

    @Schema(description = "Callback URL to receive event status",
            example = "http://localhost:8080/mock-client/event-status")
    private String callbackUrl;

    // Dynamic deserialization for payload
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "eventType",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = EmailPayload.class, name = "EMAIL"),
            @JsonSubTypes.Type(value = SmsPayload.class, name = "SMS"),
            @JsonSubTypes.Type(value = PushPayload.class, name = "PUSH")
    })
    @Schema(description = "Notification payload")
    private EventPayload payload;
}

