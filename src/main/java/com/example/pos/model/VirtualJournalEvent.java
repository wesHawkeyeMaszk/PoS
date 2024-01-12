package com.example.pos.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VirtualJournalEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long virtualJournalEventId;

    String event;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created;

    public VirtualJournalEvent(String temp) {
        event = temp;
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

}
