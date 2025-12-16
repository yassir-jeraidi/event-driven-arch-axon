package org.example.cqrs.query.entities;

import jakarta.persistence.*;
import lombok.*;
import org.example.cqrs.core.enums.AccountStatus;
import org.example.cqrs.core.enums.Currency;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    private String id;
    private double balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private Instant createdDate;
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
}
