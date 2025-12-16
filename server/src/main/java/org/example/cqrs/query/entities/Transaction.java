package org.example.cqrs.query.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.cqrs.core.enums.Currency;
import org.example.cqrs.core.enums.TransactionType;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Instant date;
    private double amount;
    private Currency currency;
    @ManyToOne
    @JsonIgnore
    private Account account;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
