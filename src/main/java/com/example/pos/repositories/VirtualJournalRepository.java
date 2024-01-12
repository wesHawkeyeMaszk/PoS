package com.example.pos.repositories;

import com.example.pos.model.Item;
import com.example.pos.model.VirtualJournal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualJournalRepository extends CrudRepository<VirtualJournal, Integer> {
}
