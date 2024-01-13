package com.example.pos.controller;


import com.example.pos.model.Item;
import com.example.pos.repository.ItemRepository;
import com.example.pos.screens.view.EmployeeView;
import com.example.pos.services.ScanService;
import com.example.pos.services.ScannerListener;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Getter
@Component
public class Register implements ScannerListener {
    @Autowired
    ScanService scanService;

    @Autowired
    EmployeeView employeeView;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    VirtualJournalEventController journalEventController;

    Boolean hasBasket;
    Basket basket;
    ArrayList<Item> itemButtonList;
    private int transactionNumber;

    public Register() {
        scanService = new ScanService();
        scanService.addScannerListener(this);
        hasBasket = false;
        this.itemButtonList = itemRepository.findQuantity(24);
        journalEventController.readyToBeginCheckout();
        transactionNumber = 0;
    }

    @Override
    public void onScan(String scan) {
        if(!hasBasket) {
            buildNewBasket();
        }
        else {
            Item item = findItem(scan);
            addItemToBasket(item);
        }
    }

    public void addMultiItemToBasket(Item itemToChangeQuantity, int quantityChange) {
        employeeView.changeQuantity(quantityChange);
        basket.addMultiItemToBasket(itemToChangeQuantity,quantityChange);
        journalEventController.addItemToTransaction(itemToChangeQuantity, String.valueOf(transactionNumber));
    }

    public void removeLastItemFromBasket() {
        Item itemRemoved = basket.removeLastItemFromBasket();
        journalEventController.voidLastItem(itemRemoved, String.valueOf(transactionNumber));
    }

    public void buildNewBasket() {
        basket = new Basket();
        hasBasket = true;
        transactionNumber++;
        journalEventController.beginTransaction(String.valueOf(transactionNumber));
    }

    public void voidTransaction() {
        if (hasBasket) {
            journalEventController.voidWholeTransaction(String.valueOf(transactionNumber));
            readyForCheckout();
        }
    }

    public void readyForCheckout() {
        hasBasket = false;
        journalEventController.readyToBeginCheckout();
    }

    public void payByCredit() {
        if (hasBasket) {
            journalEventController.payCredit(String.valueOf(transactionNumber), basket.getTotal());
            readyForCheckout();
        }
    }

    public void payExactDollar() {
        if (hasBasket) {
            journalEventController.payExactDollar(String.valueOf(transactionNumber), basket.getTotal());
            readyForCheckout();
        }
    }

    public void payNextDollar() {
        if (hasBasket) {
            BigDecimal rounded = basket.getTotal().setScale(0, RoundingMode.CEILING);
            journalEventController.payNextDollar(rounded.subtract(basket.getTotal()).setScale(2, RoundingMode.HALF_UP).toString(), String.valueOf(transactionNumber), basket.getTotal());
            readyForCheckout();
        }
    }

    public Item returnLastItem() {
        if(hasBasket){
            return basket.returnLastItem();
        }
        else
            return new Item();
    }


    public Item findItem(String upc) {
        return itemRepository.findItemByUPC(upc);
    }

    public void addItemToBasket(Item item) {
        employeeView.addLineItem(item);
        basket.addItemToBasket(item);
        journalEventController.addItemToTransaction(item, String.valueOf(transactionNumber));
    }

    public int getItemQuantity() {
        return basket.getItemQuantity();
    }

    public BigDecimal getTaxTotal() {
        return basket.getTaxTotal();
    }

    public BigDecimal getTotal() {
        return basket.getTotal();
    }

    public BigDecimal getSubtotal() {
        return basket.getSubtotal();
    }
}
