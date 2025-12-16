package org.example.cqrs.command.aggregates;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.example.cqrs.command.commands.*;
import org.example.cqrs.command.events.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.example.cqrs.core.enums.*;
import org.example.cqrs.core.services.CurrencyExchangeService;
import org.example.cqrs.core.utils.ObjectUtils;

@Slf4j
@Aggregate
@Getter
@Setter
public class AccountAggregate {
    @AggregateIdentifier
    private String id;
    private double balance;
    private AccountStatus status;
    private Currency currency;

    @Autowired
    private CurrencyExchangeService exchangeService;

    // Axon constructor
    public AccountAggregate() {
    }

    // CREATION
    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("------------------------- CREATE Account Command Received -----------------------");
        if (command.balance() < 0)
            throw new IllegalArgumentException("Balance cannot be negative");

        AggregateLifecycle.apply(
                new AccountCreatedEvent(
                        command.id(),
                        command.balance(),
                        command.currency(),
                        AccountStatus.CREATED
                ));
    }

    @EventSourcingHandler
    public void onCreation(AccountCreatedEvent event) {
        log.info("------------------------- CREATE Account Event Received -----------------------");
        this.setId(event.id());
        this.setBalance(event.balance());
        this.setCurrency(event.currency());
        this.setStatus(event.status());
    }

    // ACTIVATION
    @CommandHandler
    public void activateAccount(ActivateAccountCommand command) {
        log.info("------------------------- ACTIVATE Account Command Received -----------------------");
        if (this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account is already activated");
        if (ObjectUtils.equalsAny(status, AccountStatus.BLOCKED))
            throw new IllegalArgumentException("Account cannot be activated");

        AggregateLifecycle.apply(new AccountActivatedEvent(command.id()));
    }

    @EventSourcingHandler
    public void onActivation(AccountActivatedEvent event) {
        log.info("------------------------- ACTIVATE Account Event Received -----------------------");
        this.status = AccountStatus.ACTIVATED;
    }

    // SUSPENSION
    @CommandHandler
    public void suspendAccount(SuspendAccountCommand command) {
        log.info("------------------------- SUSPEND Account Command Received -----------------------");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account must be activated to suspend");

        AggregateLifecycle.apply(new AccountSuspendedEvent(command.id()));
    }

    @EventSourcingHandler
    public void onSuspend(AccountSuspendedEvent event) {
        log.info("------------------------- SUSPEND Account Event Received -----------------------");
        this.status = AccountStatus.SUSPENDED;
    }

    // BLOCKING
    @CommandHandler
    public void blockAccount(BlockAccountCommand command) {
        log.info("------------------------- BLOCK Account Command Received -----------------------");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account must be activated to block");

        AggregateLifecycle.apply(new AccountBlockedEvent(command.id()));

    }

    @EventSourcingHandler
    public void onBlock(AccountBlockedEvent event) {
        log.info("------------------------- BLOCK Account Event Received -----------------------");
        this.status = AccountStatus.BLOCKED;
    }

    // CREDIT
    @CommandHandler
    public void creditAccount(CreditAccountCommand command) {
        log.info("------------------------- CREDIT Account Command Received -----------------------");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account must be activated to make transactions");

        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.id(),
                exchangeService.convert(command.amount(), command.currency(), currency)
        ));
    }

    @EventSourcingHandler
    public void onCredit(AccountCreditedEvent event) {
        log.info("------------------------- CREDIT Account Event Received -----------------------");
        this.balance += event.balance();
    }

    // DEBIT
    @CommandHandler
    public void debitAccount(DebitAccountCommand command) {
        log.info("------------------------- DEBIT Account Command Received -----------------------");
        if (!this.getStatus().equals(AccountStatus.ACTIVATED))
            throw new IllegalArgumentException("Account must be activated to make transactions");

        double amount = exchangeService.convert(command.amount(), command.currency(), currency);
        if (this.balance < amount )
            throw new IllegalArgumentException("Insufficient funds");

        AggregateLifecycle.apply(new AccountDebitedEvent(command.id(), amount));
    }

    @EventSourcingHandler
    public void onDebit(AccountDebitedEvent event) {
        log.info("------------------------- DEBIT Account Event Received -----------------------");
        this.balance -= event.balance();
    }
}
