package com.example.pos.screens.view;

import com.example.pos.controller.VirtualJournalEventController;
import com.example.pos.model.Cashier;
import com.example.pos.repository.CashierRepository;
import com.example.pos.repository.ShiftRepository;
import com.example.pos.services.TSVReaderService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
@Component
public class LoginView extends JFrame implements ActionListener {


    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    CashierRepository cashierRepository;
    @Autowired
    VirtualJournalEventController eventController;
    @Autowired
    TSVReaderService tsvReaderService;

    JPanel panel;
    JLabel user_label, password_label, message;
    JTextField userName_text;
    JPasswordField password_text;
    JButton submit, cancel;
    JButton welcome;
    JButton loadNewPriceBook;
    JButton addNewCashier;
    public void setFrameUp(){
        // Username Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        userName_text = new JTextField();
        userName_text.setPreferredSize(new Dimension(50,20));
        // Password Label
        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();
        password_text.setPreferredSize(new Dimension(50,20));
        // Submit
        submit = new JButton("SUBMIT");
        panel = new JPanel(new GridLayout(3, 1));
        welcome = new JButton("WELCOME CLICK TO LOGIN IN");
        welcome.addActionListener((ActionEvent event) -> closeLogin());
        welcome.setPreferredSize(new Dimension(100,20));
        loadNewPriceBook = new JButton("WOULD YOU LIKE TO LOAD A NEW PRICE BOOK?");
        loadNewPriceBook.addActionListener((ActionEvent event) -> loadNewPriceBook() );
        loadNewPriceBook.setPreferredSize(new Dimension(100,20));
        addNewCashier = new JButton("WOULD YOU LIKE TO ADD A NEW CASHIER?");
        addNewCashier.addActionListener((ActionEvent event) -> addNewCashier() );
        addNewCashier.setPreferredSize(new Dimension(100,20));
        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);
        message = new JLabel();
        panel.add(message);
        panel.add(submit);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(400,200);
        this.setLocationRelativeTo(null);
    }

    @PostConstruct
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void prepareFrame() {

        setFrameUp();
        //pack();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userName = userName_text.getText().trim();
        String password = password_text.getText().trim();

        try {
            Cashier cashier = cashierRepository.findByName(userName);

            if (cashier.getCashierPassword().equals(password)){
                panel.removeAll();
                panel.add(welcome);
                panel.add(loadNewPriceBook);
                panel.add(addNewCashier);
                eventController.cashierLoggedIn(userName);
                panel.revalidate();
                panel.repaint();
            }
            else {
                message.setText(" Invalid user.. ");
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void loadNewPriceBook(){
        tsvReaderService.readTSV();
    }

    public void addNewCashier() {
        this.setVisible(false); //you can't see me!
    }

    public void closeLogin(){
        this.setVisible(false); //you can't see me!
        this.dispose(); //Destroy the JFrame object
    }
}
