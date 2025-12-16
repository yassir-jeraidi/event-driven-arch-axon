package org.example.cqrs.command.events;

import org.example.cqrs.core.enums.Currency;

public record AccountCreditedEvent(String id, double balance) {
}
