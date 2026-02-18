package org.poc.eventing.sms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.poc.eventing.model.EventPayload;

@Getter
@Setter
public class SmsPayload implements EventPayload {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String message;
}

