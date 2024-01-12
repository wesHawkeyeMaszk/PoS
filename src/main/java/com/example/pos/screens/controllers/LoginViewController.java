package com.example.pos.screens.controllers;

import com.example.pos.screens.view.LoginView;
import com.example.pos.screens.view.PinPadView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class LoginViewController extends AbstractViewController {

    private final LoginView loginView;
    private final EmployeeViewController employeeViewController;
    @Override
    public void prepareAndOpenFrame() {
        registerAction(loginView.getWelcome(), (e) -> openEmployeeView());
        loginView.setVisible(true);
    }

    private void openEmployeeView(){
        employeeViewController.prepareAndOpenFrame();
    }


}
