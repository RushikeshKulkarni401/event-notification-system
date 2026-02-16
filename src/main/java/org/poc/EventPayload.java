package org.poc;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "eventType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailPayload.class, name = "EMAIL"),
        @JsonSubTypes.Type(value = SmsPayload.class, name = "SMS"),
        @JsonSubTypes.Type(value = PushPayload.class, name = "PUSH")
})

public interface EventPayload {}
