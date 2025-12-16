package org.example.cqrs.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.enums.Currency;

public record CreditAccountCommand(
        @TargetAggregateIdentifier String id,
        double amount,
        Currency currency
) {}
