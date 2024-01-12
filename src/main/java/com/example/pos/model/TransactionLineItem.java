package com.example.pos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long transactionLineItemId;

    @Column(nullable = false)
    long transactionId;

    @Column(nullable = false)
    BigDecimal TransactionLineItemValue;

    @Column(columnDefinition="Boolean default FALSE")
    Boolean wasVoided;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }
}
