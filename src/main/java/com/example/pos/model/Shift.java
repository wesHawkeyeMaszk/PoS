package com.example.pos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long shiftId;

    String userName;

    BigDecimal registerTotalStartOfShift;
    BigDecimal registerTotalEndOfShift;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    BigDecimal cashTransactions;
    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    BigDecimal creditTransactions;

    @Temporal(TemporalType.TIMESTAMP)
    private Date shiftEnd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    Date shiftStart;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated", nullable = false)
    private Date updated;

    @PrePersist
    protected void onCreate() {
        updated = shiftStart = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }

    public Shift(String userName, double registerTotalStartOfShift) {
        this.userName = userName;
        this.registerTotalStartOfShift = registerTotalEndOfShift;
    }
}
