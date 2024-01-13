package com.example.pos.controller;

import com.example.pos.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
public class Basket {

    private List<Item> basket;
    private List<Integer> basketQuantity;
    private int itemQuantity;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal taxTotal;
    private BigDecimal total;

    Basket() {
        basket = new ArrayList<>();
        basketQuantity = new ArrayList<>();
        itemQuantity = 0;
        subtotal = BigDecimal.valueOf(0.00);
        tax = BigDecimal.valueOf(0.07);
        total = BigDecimal.valueOf(0.00);
        taxTotal = BigDecimal.valueOf(0.00);
    }


    public void addItemToBasket(Item item) {
        basket.add(item);
        basketQuantity.add(1);
        updateQuantity();
        BigDecimal itemSubTotal = new BigDecimal(item.getItemValue());
        BigDecimal itemTax = itemSubTotal.multiply(tax).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalPlusItemTax = itemSubTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        subtotal = subtotal.add(itemSubTotal).setScale(2, RoundingMode.HALF_UP);
        taxTotal = taxTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        total = total.add(subTotalPlusItemTax).setScale(2, RoundingMode.HALF_UP);
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
    }

    public Item removeLastItemFromBasket() {
        Item item = basket.get(basket.size() - 1);
        int itemQuantity = basketQuantity.get(basketQuantity.size() - 1);
        basket.remove(basket.size() - 1);
        basketQuantity.remove(basketQuantity.size() - 1);
        updateQuantity();
        BigDecimal itemSubTotal = new BigDecimal(item.getItemValue()).multiply(BigDecimal.valueOf(itemQuantity));
        BigDecimal itemTax = itemSubTotal.multiply(tax).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subTotalPlusItemTax = itemSubTotal.add(itemTax).setScale(2, RoundingMode.HALF_UP);
        subtotal = subtotal.subtract(itemSubTotal).setScale(2, RoundingMode.HALF_UP);
        taxTotal = taxTotal.subtract(itemTax).setScale(2, RoundingMode.HALF_UP);
        total = total.subtract(subTotalPlusItemTax).setScale(2, RoundingMode.HALF_UP);
        return item;
    }

    public void updateQuantity() {
        itemQuantity = basketQuantity.stream().mapToInt(Integer::intValue).sum();
    }

    public Item returnLastItem() {
        return basket.get(basket.size() - 1);
    }
}
