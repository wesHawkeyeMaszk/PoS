package com.example.pos.controller;

import com.example.pos.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Getter
@Setter
@AllArgsConstructor
public class Checkout {

    @Autowired
    VirtualJournalEventController journalEventController;

    private static String cashier;
    private static Boolean isInCheckout;
    private static List<Item> basket;
    private static List<Integer> basketQuantity;
    private static int itemQuantity;
    private static BigDecimal subtotal;
    private static BigDecimal tax;
    private static BigDecimal taxTotal;
    private static BigDecimal total;
    private static int transactionNumber = 0;


    public void readyForCheckout() {
        isInCheckout = false;
        basket = new ArrayList<>();
        basketQuantity = new ArrayList<>();
        itemQuantity = 0;
        subtotal = BigDecimal.valueOf(0.00);
        tax = BigDecimal.valueOf(0.07);
        total = BigDecimal.valueOf(0.00);
        taxTotal = BigDecimal.valueOf(0.00);
        journalEventController.readyToBeginCheckout();
    }

    public void beginCheckout() {
        isInCheckout = true;
        transactionNumber++;
        journalEventController.beginTransaction(String.valueOf(transactionNumber));
    }


    public void addItemToBasket(Item item) {
        if (basket.isEmpty() && !isInCheckout) {
            beginCheckout();
        }
        basket.add(item);
        basketQuantity.add(1);
        updateQuantity();
        BigDecimal itemSubTotal = new BigDecimal(item.getItemValue());
        BigDecimal itemTax = itemSubTotal.multiply(tax).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalPlusItemTax = itemSubTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        subtotal = subtotal.add(itemSubTotal).setScale(2, RoundingMode.HALF_UP);
        taxTotal = taxTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        total = total.add(subTotalPlusItemTax).setScale(2, RoundingMode.HALF_UP);
        journalEventController.addItemToTransaction(item, String.valueOf(transactionNumber));
    }

    public void addMultiItemToBasket(Item item, int itemQuantity) {
        basket.add(item);
        basketQuantity.add(itemQuantity);
        updateQuantity();
        BigDecimal itemSubTotal = new BigDecimal(item.getItemValue()).multiply(BigDecimal.valueOf(itemQuantity));
        BigDecimal itemTax = itemSubTotal.multiply(tax).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalPlusItemTax = itemSubTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        subtotal = subtotal.add(itemSubTotal).setScale(2, RoundingMode.HALF_UP);
        taxTotal = taxTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        total = total.add(subTotalPlusItemTax).setScale(2, RoundingMode.HALF_UP);
        journalEventController.addItemToTransaction(item, String.valueOf(transactionNumber));
    }


    public void removeLastItemFromBasket() {
        Item item = basket.get(basket.size() - 1);
        int itemQuantity = basketQuantity.get(basketQuantity.size()-1);
        basket.remove(basket.size() - 1);
        basketQuantity.remove(basketQuantity.size()-1);
        updateQuantity();
        BigDecimal itemSubTotal = new BigDecimal(item.getItemValue()).multiply(BigDecimal.valueOf(itemQuantity));
        BigDecimal itemTax = itemSubTotal.multiply(tax).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalPlusItemTax = itemSubTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        subtotal = subtotal.subtract(itemSubTotal).setScale(2, RoundingMode.HALF_UP);
        taxTotal = taxTotal.subtract(itemTax).setScale(2, RoundingMode.HALF_UP);
        total = total.subtract(subTotalPlusItemTax).setScale(2, RoundingMode.HALF_UP);
        journalEventController.voidLastItem(item, String.valueOf(transactionNumber));
    }

    public void voidTransaction() {
        if (isInCheckout) {
            journalEventController.voidWholeTransaction(String.valueOf(transactionNumber));
            readyForCheckout();
        }
    }

    public void payByCredit() {
        if (isInCheckout) {
            journalEventController.payCredit(String.valueOf(transactionNumber), total);
            readyForCheckout();
        }
    }

    public void payExactDollar() {
        if (isInCheckout) {
            journalEventController.payExactDollar(String.valueOf(transactionNumber), total);
            readyForCheckout();
        }
    }

    public void payNextDollar() {
        if (isInCheckout) {
            BigDecimal rounded = total.setScale(0, RoundingMode.CEILING);
            journalEventController.payNextDollar(rounded.subtract(total).setScale(2, RoundingMode.HALF_UP).toString(), String.valueOf(transactionNumber), total);
            readyForCheckout();
        }
    }


    public void updateQuantity() {
        itemQuantity = basketQuantity.stream().mapToInt(Integer::intValue).sum();
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Item returnLastItem(){
            return basket.get(basket.size() - 1);

    }

    public int getTransactionNumber() {
        return transactionNumber;
    }
}
