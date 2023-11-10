package com.highload.course.ga.service.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Configuration {
    @Value("${ga.measurement-protocol.url}")
    private String gaMeasurementProtocolUrl;
    @Value("${ga.measurement-protocol.api-secret}")
    private String apiSecret;
    @Value("${ga.measurement-protocol.measurement-id}")
    private String measurementId;

    @Value("${ga.measurement-protocol.client-id}")
    private String clientId;
    @Value("${ga.measurement-protocol.event-name}")
    private String name;
    @Value("${ga.measurement-protocol.debug-mode}")
    private boolean debugMode;
    @Value("${exchange-rate.uri:https://bank.gov.ua/NBUStatService/v1/statdirectory/exchangenew?json&valcode={currency_code}}")
    private String exchangeRateUri;

    @Value("${exchange-rate.currency:USD}")
    private String currencyCode;
}