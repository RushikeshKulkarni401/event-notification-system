package org.poc;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SmsPayload implements EventPayload {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String message;

    @NotBlank
    private String callBackUrl;
}

