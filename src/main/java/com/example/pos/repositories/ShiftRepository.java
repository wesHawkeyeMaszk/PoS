package com.example.pos.repositories;

import com.example.pos.model.Item;
import com.example.pos.model.Shift;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ShiftRepository extends CrudRepository<Shift, Integer> {
    @Query(nativeQuery = true, value = "select * from TEST.PUBLIC.SHIFT where USER_NAME = :username Order By CREATED Desc LIMIT 1")
    Shift findByUsername(String username);
}
