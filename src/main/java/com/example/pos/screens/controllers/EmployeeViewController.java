package com.example.pos.screens.controllers;

import com.example.pos.screens.view.EmployeeView;
import com.example.pos.services.ScanService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

@Controller
@AllArgsConstructor
public class EmployeeViewController extends AbstractViewController {
    @Autowired
    private final EmployeeView employeeView;

    @Autowired
    private final PinPadViewController pinPadViewController;

    @Override
    public void prepareAndOpenFrame() {
        employeeView.setVisible(true);
        registerAction(employeeView.getChangeQuantityButton(), (e) -> openPinPadView());
        setFocusManager();
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                employeeView.changeQuantity(pinPadViewController.getFrame().changeQuantityAmount);
            }
        };
        pinPadViewController.getFrame().addWindowListener(listener);
    }

    public void setFocusManager() {
        KeyboardFocusManager manager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(employeeView.getScanner());
        employeeView.getCheckout().readyForCheckout();
    }

    private void openPinPadView() {
        pinPadViewController.prepareAndOpenFrame();
    }


}
