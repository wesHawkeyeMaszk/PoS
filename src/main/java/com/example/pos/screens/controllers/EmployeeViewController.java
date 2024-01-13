package com.example.pos.screens.controllers;

import com.example.pos.screens.view.EmployeeView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

@Controller
@AllArgsConstructor
public class EmployeeViewController extends AbstractViewController {
    @Autowired
    EmployeeView employeeView;

    @Autowired
    KeypadViewController keypadViewController;

    @Override
    public void prepareAndOpenFrame() {
        employeeView.setVisible(true);
        registerAction(employeeView.getChangeQuantityButton(), (e) -> openPinPadView());
        setFocusManager();
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                employeeView.changeQuantity(keypadViewController.getFrame().changeQuantityAmount);
            }
        };
        keypadViewController.getFrame().addWindowListener(listener);
    }

    public void setFocusManager() {
        KeyboardFocusManager manager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(employeeView.getRegister().getScanService());
        employeeView.getRegister().readyForCheckout();
    }

    private void openPinPadView() {
        keypadViewController.prepareAndOpenFrame();
    }


}
