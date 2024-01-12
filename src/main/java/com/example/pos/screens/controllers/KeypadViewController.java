package com.example.pos.screens.controllers;

import com.example.pos.screens.view.KeypadView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class KeypadViewController extends AbstractViewController {

    @Autowired
    KeypadView keypadView;
    @Override
    public void prepareAndOpenFrame() {
        keypadView.setVisible(true);
    }

    public KeypadView getFrame(){
        return keypadView;
    }
}
