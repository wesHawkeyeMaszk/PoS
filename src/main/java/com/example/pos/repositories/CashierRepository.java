package com.example.pos.repositories;

import com.example.pos.model.Cashier;
import com.example.pos.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CashierRepository extends JpaRepository<Cashier, Integer>, CrudRepository <Cashier, Integer> {
    @Query(nativeQuery = true, value = "select * from TEST.PUBLIC.CASHIER where CASHIER_NAME = :name limit 1")
    Cashier findByName(String name);

    @Query(nativeQuery = true, value = "select case when exists( select * from TEST.PUBLIC.CASHIER where CASHIER_NAME = :name) THEN CAST (1 AS BIT)" +
            " ELSE CAST (0 AS BIT) END")
    Boolean findIfExists(String name);
}
