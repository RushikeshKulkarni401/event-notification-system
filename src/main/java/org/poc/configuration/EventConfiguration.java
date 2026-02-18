package org.poc.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class EventConfiguration {
    @Value("${event.processors.email.delaySeconds}")
    private Long delayForEmail;

    @Value("${event.processors.sms.delaySeconds}")
    private Long delayForSms;

    @Value("${event.processors.push.delaySeconds}")
    private Long delayForPush;

    @Value("${event.processors.email.failureRate}")
    private Double failureRateForEmail;

    @Value("${event.processors.email.failureRate}")
    private Double failureRateForSms;

    @Value("${event.processors.email.failureRate}")
    private Double failureRateForPush;

}
