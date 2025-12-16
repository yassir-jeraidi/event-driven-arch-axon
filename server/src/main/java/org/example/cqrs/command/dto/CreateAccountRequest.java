package org.example.cqrs.command.dto;

import org.example.cqrs.core.enums.Currency;

public record CreateAccountRequest(double balance, Currency currency) {
}
