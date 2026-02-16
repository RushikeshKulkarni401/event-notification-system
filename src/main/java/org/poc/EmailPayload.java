package org.poc;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailPayload implements EventPayload {

    @NotBlank
    private String recipient;

    @NotBlank
    private String message;

    @NotBlank
    private String callBackUrl;
}