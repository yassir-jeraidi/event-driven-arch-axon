package org.example.cqrs.query.handlers;

import org.axonframework.queryhandling.QueryHandler;
import org.example.cqrs.query.entities.Account;
import org.example.cqrs.query.queries.GetAccountQuery;
import org.example.cqrs.query.queries.GetAllAccountsQuery;
import org.example.cqrs.query.repositories.AccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountQueryHandler {
    private AccountRepository accountRepository;

    public AccountQueryHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @QueryHandler
    public List<Account> onGetAllAccountsQuery(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public Account onGetAccountQuery(GetAccountQuery query) {
        return accountRepository.findById(query.id()).orElseThrow();
    }
}
