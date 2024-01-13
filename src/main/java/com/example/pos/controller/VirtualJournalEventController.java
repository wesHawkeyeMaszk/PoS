package com.example.pos.controller;

import com.example.pos.model.Item;
import com.example.pos.model.Shift;
import com.example.pos.model.VirtualJournalEvent;
import com.example.pos.repository.CashierRepository;
import com.example.pos.repository.ShiftRepository;
import com.example.pos.repository.VirtualJournalEventRepository;
import com.example.pos.sockets.Server;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Getter
@Setter
public class VirtualJournalEventController {

    @Autowired
    VirtualJournalEventRepository eventRepository;
    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    CashierRepository cashierRepository;
    @Autowired
    Server server;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
    String register = "REGISTER1";
    String cashier = "DARTH VADER";
    Shift shift;

    public void readyToBeginCheckout() {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "WAITING TO BEGIN NEW TRANSACTION\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void beginTransaction(String transactionNumber) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "STARTING TRANSACTION#" + transactionNumber + "\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void transactionComplete(String transactionNumber) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tCOMPLETE\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void addItemToTransaction(Item item, String transactionNumber) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tADD ITEM\t" + item.getItemName() + "\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void addMultiItemToTransaction(Item item, String transactionNumber, int itemQuantity) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tADDED"+itemQuantity+"\t" + item.getItemName() + "\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void voidLastItem(Item item, String transactionNumber) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tVOID PREVIOUS ITEM\t" + item.getItemName() + "\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void voidWholeTransaction(String transactionNumber) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tTRANSACTION HAS BEEN VOIDED\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }

    public void payExactDollar(String transactionNumber, BigDecimal total) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tPAYED EXACT DOLLAR\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
        //shift.setCashTransactions(shift.getCashTransactions().add(total));
    }

    public void payNextDollar(String change, String transactionNumber, BigDecimal total) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tPAYED NEXT DOLLAR\t GAVE CUSTOMER " + change + " IN CHANGE\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
        //shift.setCashTransactions(shift.getCashTransactions().add(total));

    }

    public void payCredit(String transactionNumber, BigDecimal total) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + cashier + "\t" + "TRANSACTION#" + transactionNumber + "\tPAYED CREDIT\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
        //shift.setCreditTransactions(shift.getCreditTransactions().add(total));
    }

    public void cashierLoggedIn(String userName) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + register + "\t" + userName + "\t" + "HAS LOGGED IN REGISTER TOTAL:\t" + 100.00 + "\n";
        System.out.print(temp);
        shiftRepository.save(new Shift(userName, 100.00));
        shift = shiftRepository.findByUsername(userName);
        eventRepository.save(new VirtualJournalEvent(temp));
        cashier = userName;
        server.broadcast(temp);
    }

    public void newCashierAdded(String userName) {
        String date = simpleDateFormat.format(new Date());
        String temp = date + "\t" + userName + "\t" + "HAS BEEN ADDED TO THE SYSTEM\n";
        System.out.print(temp);
        eventRepository.save(new VirtualJournalEvent(temp));
        server.broadcast(temp);
    }
}
