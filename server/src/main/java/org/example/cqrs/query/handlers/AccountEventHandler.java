package org.example.cqrs.query.handlers;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.example.cqrs.command.events.*;
import org.example.cqrs.query.entities.Account;
import org.example.cqrs.query.entities.Transaction;
import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.enums.TransactionType;
import org.example.cqrs.query.repositories.AccountRepository;
import org.example.cqrs.query.repositories.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountEventHandler {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountEventHandler(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @EventHandler
    public void onCreate(AccountCreatedEvent event, EventMessage eventMessage) {
        log.info("Query Side: Account Created Event Received");
        Account account = Account.builder()
                .id(event.id())
                .balance(event.balance())
                .currency(event.currency())
                .status(event.status())
                .createdDate(eventMessage.getTimestamp())
                .build();

        accountRepository.save(account);
    }

    @EventHandler
    public void onCredit(AccountCreditedEvent event, EventMessage eventMessage) {
        log.info("Query Side: Account Credited Event Received");
        Account account = accountRepository.findById(event.id()).orElseThrow();
        Transaction transaction = Transaction.builder()
                .date(eventMessage.getTimestamp())
                .amount(event.balance())
                .currency(account.getCurrency())
                .account(account)
                .type(TransactionType.CREDIT)
                .build();
        transactionRepository.save(transaction);
        account.setBalance(account.getBalance() + event.balance());
        accountRepository.save(account);
    }

    @EventHandler
    public void onDebit(AccountDebitedEvent event, EventMessage eventMessage) {
        log.info("Query Side: Account Debited Event Received");
        Account account = accountRepository.findById(event.id()).orElseThrow();
        Transaction transaction = Transaction.builder()
                .date(eventMessage.getTimestamp())
                .amount(event.balance())
                .currency(account.getCurrency())
                .account(account)
                .type(TransactionType.DEBIT)
                .build();
        transactionRepository.save(transaction);
        account.setBalance(account.getBalance() - event.balance());
        accountRepository.save(account);
    }

    @EventHandler
    public void onActivate(AccountActivatedEvent event) {
        log.info("Query Side: Account Activated Event Received");
        Account account = accountRepository.findById(event.id()).orElseThrow();
        account.setStatus(AccountStatus.ACTIVATED);
        accountRepository.save(account);
    }

    @EventHandler
    public void onSuspend(AccountSuspendedEvent event) {
        log.info("Query Side: Account Suspended Event Received");
        Account account = accountRepository.findById(event.id()).orElseThrow();
        account.setStatus(AccountStatus.SUSPENDED);
        accountRepository.save(account);
    }

    @EventHandler
    public void onBlock(AccountBlockedEvent event) {
        log.info("Query Side: Account Blocked Event Received");
        Account account = accountRepository.findById(event.id()).orElseThrow();
        account.setStatus(AccountStatus.BLOCKED);
    }
}
