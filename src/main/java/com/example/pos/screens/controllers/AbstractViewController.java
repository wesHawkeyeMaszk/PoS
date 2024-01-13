package com.example.pos.screens.controllers;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class AbstractViewController {

    public abstract void prepareAndOpenFrame();

    protected void registerAction(JButton button, ActionListener listener) {
        button.addActionListener(listener);
    }

}