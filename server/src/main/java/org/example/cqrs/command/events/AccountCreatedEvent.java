package org.example.cqrs.command.events;

import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.enums.Currency;

public record AccountCreatedEvent(String id, double balance, Currency currency, AccountStatus status) {
}
