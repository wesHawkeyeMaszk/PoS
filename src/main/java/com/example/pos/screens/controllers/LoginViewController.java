package com.example.pos.screens.controllers;

import com.example.pos.screens.view.AddNewCashierView;
import com.example.pos.screens.view.LoginView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Controller
@AllArgsConstructor
public class LoginViewController extends AbstractViewController {

    @Autowired
    LoginView loginView;

    @Autowired
    AddNewCashierController addNewCashierController;

    @Autowired
    EmployeeViewController employeeViewController;
    @Override
    public void prepareAndOpenFrame() {
        registerAction(loginView.getWelcome(), (e) -> openEmployeeView());
        registerAction(loginView.getAddNewCashier(), (e) -> openAddNewCashierView());

        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                loginView.setVisible(true);
            }
        };
        addNewCashierController.getFrame().addWindowListener(listener);
        loginView.setVisible(true);

        /*
            WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                employeeView.changeQuantity(keypadViewController.getFrame().changeQuantityAmount);
            }
        };
        keypadViewController.getFrame().addWindowListener(listener);
         */

    }

    private void openEmployeeView(){
        employeeViewController.prepareAndOpenFrame();
    }

    private void openAddNewCashierView() {
        addNewCashierController.prepareAndOpenFrame();
    }
}
