package org.example.cqrs.command.commands;


import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.example.cqrs.core.enums.Currency;

public record CreateAccountCommand(
        @TargetAggregateIdentifier String id,
        double balance,
        Currency currency
) {
}
