package com.example.pos.repository;

import com.example.pos.model.VirtualJournalEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualJournalEventRepository extends CrudRepository<VirtualJournalEvent, Integer> {
}
