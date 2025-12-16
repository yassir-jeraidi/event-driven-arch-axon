package org.example.cqrs.core.controllers;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("/events")
public class EventConotroller {
    private final EventStore eventStore;

    public EventConotroller(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @GetMapping("/{accountId}")
    public Stream eventStore(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }
}
