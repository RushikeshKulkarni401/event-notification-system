package org.poc;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRequest {
    private EventType eventType;
    private String callbackUrl;

    @JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "eventType",
            visible = true
    )
    @JsonSubTypes({
            @JsonSubTypes.Type(value = EmailPayload.class, name = "EMAIL"),
            @JsonSubTypes.Type(value = SmsPayload.class, name = "SMS"),
            @JsonSubTypes.Type(value = PushProcessor.class, name = "PUSH")
    })
    private EventPayload payload;
}

