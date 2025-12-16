package org.example.cqrs.command.dto;

import org.example.cqrs.core.enums.Currency;

public record CreditAccountRequest(String id, double amount, Currency currency) {
}
