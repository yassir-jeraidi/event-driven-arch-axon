package org.example.cqrs.query.controllers;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.example.cqrs.query.entities.Account;
import org.example.cqrs.query.queries.GetAccountQuery;
import org.example.cqrs.query.queries.GetAllAccountsQuery;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/query/account")
@CrossOrigin(origins = "*")
public class AccountQueryController {
    private final QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping
    public CompletableFuture<List<Account>> getAccounts() {
        return queryGateway.query(new GetAllAccountsQuery(), ResponseTypes.multipleInstancesOf(Account.class));
    }

    @GetMapping("/{id}")
    public CompletableFuture<Account> getAccount(@PathVariable String id) {
        return queryGateway.query(new GetAccountQuery(id), ResponseTypes.instanceOf(Account.class));
    }
}
