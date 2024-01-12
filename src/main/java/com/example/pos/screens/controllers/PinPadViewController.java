package com.example.pos.screens.controllers;

import com.example.pos.screens.view.PinPadView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class PinPadViewController extends AbstractViewController {
    
    private final PinPadView pinPadView;
    @Override
    public void prepareAndOpenFrame() {
        pinPadView.setVisible(true);
    }

    public PinPadView getFrame(){
        return pinPadView;
    }
}
