package com.example.pos.screens.view;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

@Getter
@Component
@AllArgsConstructor
public class KeypadView extends JFrame implements ActionListener {
    JPanel numberPanel;
    JLabel display;
    JButton enter;
    JButton numButton;
    JButton clearButton;
    String displayContent = "";
    String[] numPadContent = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0",};
    ArrayList<JButton> buttonList;
    public int changeQuantityAmount = 0;

    public KeypadView() {}

    @PostConstruct
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void prepareFrame() {
        setFrameUp();
    }

    public void setFrameUp(){
        numberPanel = new JPanel();
        numberPanel.setLayout(new GridLayout(4, 4, 0, 0));
        numberPanel.setPreferredSize(new Dimension(320, 260));

        display = new JLabel(displayContent);
        display.setPreferredSize(new Dimension(320, 25));
        display.setBorder(BorderFactory.createLoweredBevelBorder());
        numberPanel.add(display, BorderLayout.PAGE_START);

        buttonList = new ArrayList<>(12);

        for (String s : numPadContent) {
            numButton = new JButton(s);
            buttonList.add(numButton);
        }
        for (JButton jButton : buttonList) {
            jButton.addActionListener(this);
            numberPanel.add(jButton);
        }

        numberPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.black));

        enter = new JButton("Enter");
        enter.setPreferredSize(new Dimension(320, 30));
        enter.addActionListener(this);
        numberPanel.add(enter);
        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(320, 30));
        clearButton.addActionListener(this);
        numberPanel.add(clearButton, BorderLayout.PAGE_END);
        add(numberPanel, BorderLayout.CENTER);
        setSize(450,350);
    }

    public void actionPerformed(ActionEvent e) {
        String textThere = display.getText();
        String additionalText = "";
        for (JButton jButton : buttonList) {
            if (e.getSource().equals(jButton)) {
                additionalText = jButton.getText();
            }
        }

        if (e.getSource().equals(clearButton)) {
            textThere = "";
        }

        if (e.getSource().equals(enter)) {
            changeQuantityAmount = Integer.parseInt(display.getText());
            textThere = "";
            this.setVisible(false); //you can't see me!
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
        display.setText(textThere.concat(additionalText));

    }
}
