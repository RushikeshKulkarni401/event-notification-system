package org.poc.eventing.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.poc.eventing.email.EmailPayload;
import org.poc.eventing.push.PushPayload;
import org.poc.eventing.sms.SmsPayload;

@Getter
@Setter
public class EventRequest {
    private EventType eventType;
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
    private EventPayload payload;
}

