package org.poc.eventing.push;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.poc.eventing.model.EventPayload;

@Getter
@Setter
public class PushPayload implements EventPayload {

    @NotBlank
    private String deviceId;

    @NotBlank
    private String message;
}

