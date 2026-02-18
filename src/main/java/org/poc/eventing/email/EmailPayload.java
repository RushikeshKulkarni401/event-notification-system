package org.poc.eventing.email;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.poc.eventing.model.EventPayload;

@Getter
@Setter
public class EmailPayload implements EventPayload {

    @NotBlank
    private String recipient;

    @NotBlank
    private String message;
}