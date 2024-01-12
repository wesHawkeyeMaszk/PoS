package com.example.pos.screens.controllers;

import com.example.pos.screens.view.AddNewCashierView;
import com.example.pos.screens.view.KeypadView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class AddNewCashierController extends AbstractViewController {

    @Autowired
    AddNewCashierView newCashierView;

    @Override
    public void prepareAndOpenFrame() {
        newCashierView.setVisible(true);
    }

    public AddNewCashierView getFrame(){
        return newCashierView;
    }

}
