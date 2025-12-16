package org.example.cqrs.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ActivateAccountCommand(@TargetAggregateIdentifier String id) {
}
