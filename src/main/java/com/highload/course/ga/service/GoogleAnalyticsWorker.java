package com.highload.course.ga.service;

import com.highload.course.ga.service.config.Configuration;
import com.highload.course.ga.service.model.ExchangeRate;
import com.highload.course.ga.service.model.GoogleAnalyticsEvent;
import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class GoogleAnalyticsWorker {
    private static final Logger logger = LoggerFactory.getLogger(GoogleAnalyticsWorker.class);
    private final WebClient webClient;
    private final Configuration config;

    public GoogleAnalyticsWorker(Configuration configuration) {
        this.config = configuration;

        HttpClient httpClient = HttpClient
                .create()
                .wiretap(this.getClass().getCanonicalName(),
                        LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL);

        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Scheduled(cron = "${cron.expression}")
    public void collectAndSendGAMP() {
        LocalDate today = LocalDate.now();

        Optional<ExchangeRate> exchangeRateOptional = getExchangeRateFromNBU();

        if (exchangeRateOptional.isEmpty()) {
            logger.error("{} exchange Rate for {} is not found", config.getCurrencyCode(), today);
        } else {
            ExchangeRate exchangeRate = exchangeRateOptional.get();
            logger.info("{} exchange Rate for {} - {}", config.getCurrencyCode(), today, exchangeRate);

            GoogleAnalyticsEvent gaEvent = new GoogleAnalyticsEvent(config.getClientId(),
                    config.getName(), exchangeRate.getRate(), config.isDebugMode(), today);

            sendMeasurementProtocolEvent(gaEvent);

            logger.info("Event Exchange Rate {} was sent to Google Analytics", gaEvent);
        }
    }

    private Optional<ExchangeRate> getExchangeRateFromNBU() {
        List<ExchangeRate> exchangeRate =
                webClient.get().uri(config.getExchangeRateUri(), config.getCurrencyCode())
                        .retrieve().bodyToMono(new ParameterizedTypeReference<List<ExchangeRate>>() {}).block();

        return exchangeRate.stream().findFirst();
    }

    private void sendMeasurementProtocolEvent(GoogleAnalyticsEvent gaEvent) {
        webClient.post()
                .uri(config.getGaMeasurementProtocolUrl(),
                        config.getApiSecret(), config.getMeasurementId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(gaEvent))
                .retrieve().bodyToMono(Void.class).block();
    }
}