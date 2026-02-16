package org.poc;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushPayload implements EventPayload {

    @NotBlank
    private String deviceId;

    @NotBlank
    private String message;
}

