package com.example.pos.repository;

import com.example.pos.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ItemRepository extends CrudRepository<Item, Integer> {
    @Query(nativeQuery = true, value = "select * from TEST.PUBLIC.ITEM LIMIT :quantity")
    ArrayList<Item> findQuantity(int quantity);


    @Query(nativeQuery = true, value = "select * from TEST.PUBLIC.ITEM where ITEM_UPC = :upc ")
    Item findItemByUPC(String upc);
    /*
    SELECT code, sum(quantity) as qty
FROM LineItems
WHERE dayID = 20240108
GROUP BY code
ORDER BY qty DESC
LIMIT 1000

     */
}
