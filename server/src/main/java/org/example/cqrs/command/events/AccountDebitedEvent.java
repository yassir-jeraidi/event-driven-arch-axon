package org.example.cqrs.command.events;

public record AccountDebitedEvent(String id, double balance) {
}
