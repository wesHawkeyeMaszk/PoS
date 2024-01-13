package com.example.pos.screens.view;

import com.example.pos.controller.VirtualJournalEventController;
import com.example.pos.model.Cashier;
import com.example.pos.repositories.CashierRepository;
import com.example.pos.repositories.ShiftRepository;
import com.example.pos.services.TSVReaderService;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

@Getter
@Component
public class AddNewCashierView extends JFrame implements ActionListener {


    @Autowired
    ShiftRepository shiftRepository;
    @Autowired
    CashierRepository cashierRepository;
    @Autowired
    VirtualJournalEventController eventController;
    @Autowired
    TSVReaderService tsvReaderService;

    JPanel panel;
    JLabel user_label, password_label, message, confirmPassword_label;
    JTextField userName_text;
    JPasswordField password_text, confirmPasswordText;
    JButton submit, cancel;
    JButton welcome;
    JButton loadNewPriceBook;
    public void setFrameUp(){
        // Username Label
        user_label = new JLabel();
        user_label.setText("Enter New User Name :");
        userName_text = new JTextField();
        // Password Label
        password_label = new JLabel();
        password_label.setText("Enter Password :");
        password_text = new JPasswordField();

        confirmPassword_label = new JLabel();
        confirmPassword_label.setText("Confirm Password :");
        confirmPasswordText = new JPasswordField();
        // Submit
        submit = new JButton("SUBMIT");
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        welcome = new JButton("WELCOME CLICK TO RETURN TO LOGIN MENU");
        welcome.addActionListener((ActionEvent event) -> closeAddNewCashier());
        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);
        panel.add(confirmPassword_label);
        panel.add(confirmPasswordText);
        message = new JLabel();
        panel.add(message);
        panel.add(submit);
        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(600,350);
    }

    @PostConstruct
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void prepareFrame() {
        setFrameUp();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userName = userName_text.getText().trim();
        String password = password_text.getText().trim();
        String confirmPassword = confirmPasswordText.getText().trim();

        try {
            Boolean cashierExists = cashierRepository.findIfExists(userName);
            if (!cashierExists&&password.equals(confirmPassword)){
                panel.removeAll();
                panel.add(welcome);
                eventController.newCashierAdded(userName);
                cashierRepository.save(new Cashier(userName,password));
                panel.revalidate();
                panel.repaint();
            }
            else if (cashierExists){
                message.setText(" User already exsits ");
            }
            else if(!password.equals(confirmPassword)){
                message.setText(" Passwords must match ");

            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public void closeAddNewCashier(){
        this.setVisible(false); //you can't see me!
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}