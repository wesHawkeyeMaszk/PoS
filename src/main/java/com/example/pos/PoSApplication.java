package com.example.pos;

import com.example.pos.controller.Register;
import com.example.pos.screens.controllers.LoginViewController;
import com.example.pos.sockets.Server;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class PoSApplication extends JFrame {

    public static void main(String... args) {
        ConfigurableApplicationContext context = createApplicationContext(args);
        displayLoginFrame(context);
        try {
            Server server = new Server();
            server.start(5000);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private static ConfigurableApplicationContext createApplicationContext(String... args) {
        return new SpringApplicationBuilder(PoSApplication.class)
                .headless(false)
                .run(args);
    }

    private static void displayLoginFrame(ConfigurableApplicationContext context) {
        SwingUtilities.invokeLater(() -> {
            LoginViewController loginViewController = context.getBean(LoginViewController.class);
            loginViewController.prepareAndOpenFrame();
        });
    }
}
