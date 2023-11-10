package com.highload.course.ga.service.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class GoogleAnalyticsEvent {
    private String clientId;
    private List<Event> events;

    public GoogleAnalyticsEvent(String clientId, String name, double rate, boolean debugMode, LocalDate date) {
        this.clientId = clientId;
        this.events = List.of(new Event(name, rate, debugMode, date));
    }
}

@Data
class Event {
    private String name;
    private Params params;

    public Event(String name, double rate, boolean debugMode, LocalDate date) {
        this.name = name;
        this.params = new Params(rate, debugMode, date);
    }
}

@Data
class Params {
    private double rate;
    private boolean debugMode;
    private String date;

    public Params(double rate, boolean debugMode, LocalDate date) {
        this.rate = rate;
        this.debugMode = debugMode;
        this.date = date.format(DateTimeFormatter.ISO_DATE);
    }
}

