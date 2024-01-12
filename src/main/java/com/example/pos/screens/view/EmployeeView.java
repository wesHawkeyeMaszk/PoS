package com.example.pos.screens.view;

import com.example.pos.controller.Checkout;
import com.example.pos.model.Item;
import com.example.pos.repositories.ItemRepository;
import com.example.pos.services.ScanService;
import com.example.pos.services.ScannerListener;
import jakarta.annotation.PostConstruct;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


@org.springframework.stereotype.Component
@Getter
public class EmployeeView extends JFrame implements ScannerListener {

    private final ScanService scanner;

    private final ItemRepository itemRepository;


    private final Checkout checkout;
    private final JPanel bottomQuickButtonPanel = new JPanel();
    private final JPanel rightQuickButtonPanel = new JPanel();
    private final JPanel centerPanel = new JPanel();
    private final JPanel leftReceiptPanel = new JPanel();
    private final JLabel quantity = new JLabel("QUANTITY:\t");
    private final JLabel subtotal = new JLabel("SUBTOTAL:\t");
    private final JLabel tax = new JLabel("TAX:\t");
    private final JLabel total = new JLabel("TOTAL:\t");
    JButton changeQuantityButton;
    private ArrayList<Item> itemList;

    public EmployeeView(ScanService scanner, ItemRepository itemRepository, Checkout checkout) {
        this.scanner = scanner;
        this.itemRepository = itemRepository;
        this.checkout = checkout;
    }

    @PostConstruct
    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private void prepareFrame() {
        scanner.addScannerListener(this);
        this.itemList = itemRepository.findQuantity(24);
        setFrameUp();
    }


    public void onScan(String scan) {
        if(scan.contains("QUANTITY CHANGE:")) {
            changeQuantity(Integer.parseInt(scan.substring("QUANTITY CHANGE:".length(),scan.length())));
        }
        else {
            addLineItem(itemRepository.findItemByUPC(scan));
        }
    }

    private void setFrameUp() {
        Container mainContainer = this.getContentPane();
        mainContainer.setLayout(new BorderLayout(8, 6));
        mainContainer.setBackground(Color.YELLOW);
        this.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLUE));

        //SET PANEL LAYOUTS
        bottomQuickButtonPanel.setLayout(new GridLayout(2, 6));
        rightQuickButtonPanel.setLayout(new GridLayout(3, 4));
        leftReceiptPanel.setLayout(new BoxLayout(leftReceiptPanel, BoxLayout.Y_AXIS));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        bottomQuickButtonPanel.setBackground(Color.CYAN);
        bottomQuickButtonPanel.setBorder(new LineBorder(Color.DARK_GRAY, 3));
        addBottomButtons();
        mainContainer.add(bottomQuickButtonPanel, BorderLayout.SOUTH);

        changeQuantityButton = new JButton("CHANGE QUANTITY");
        JButton payExactDollarButton = new JButton("PAY EXACT DOLLAR");
        payExactDollarButton.addActionListener((ActionEvent event) -> payExactDollar());
        JButton payNextDollarButton = new JButton("PAY NEXT DOLLAR");
        payNextDollarButton.addActionListener((ActionEvent event) -> payNextDollar());
        JButton payCreditButton = new JButton("PAY CREDIT");
        payCreditButton.addActionListener((ActionEvent event) -> payByCredit());
        JButton voidButton = new JButton("VOID LAST ITEM");
        voidButton.addActionListener((ActionEvent event) -> removeLineItem());
        JButton voidCheckoutButton = new JButton("VOID TRANSACTION");
        voidCheckoutButton.addActionListener((ActionEvent event) -> voidTransaction());

        addRightButtons();
        rightQuickButtonPanel.add(changeQuantityButton);
        rightQuickButtonPanel.add(payExactDollarButton);
        rightQuickButtonPanel.add(payNextDollarButton);
        rightQuickButtonPanel.add(payCreditButton);
        rightQuickButtonPanel.add(voidButton);
        rightQuickButtonPanel.add(voidCheckoutButton);
        rightQuickButtonPanel.setBackground(Color.PINK);
        rightQuickButtonPanel.setBorder(new LineBorder(Color.DARK_GRAY, 3));
        mainContainer.add(rightQuickButtonPanel, BorderLayout.EAST);


        JLabel register = new JLabel("Register");
        register.setAlignmentX(Component.CENTER_ALIGNMENT);
        register.setFont(new Font("Courier New", Font.BOLD, 36));
        register.setBorder(new LineBorder(Color.DARK_GRAY, 3));
        JLabel status = new JLabel("Open");
        status.setAlignmentX(Component.CENTER_ALIGNMENT);
        status.setVerticalAlignment(JLabel.TOP);
        status.setFont(new Font("Courier New", Font.BOLD, 36));
        status.setBorder(new LineBorder(Color.DARK_GRAY, 3));

        leftReceiptPanel.add(register);
        leftReceiptPanel.add(status);

        leftReceiptPanel.setBackground(Color.GREEN);
        leftReceiptPanel.setBorder(new LineBorder(Color.DARK_GRAY, 3));
        mainContainer.add(leftReceiptPanel, BorderLayout.WEST);


        subtotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtotal.setFont(new Font("Courier New", Font.BOLD, 24));
        tax.setAlignmentX(Component.CENTER_ALIGNMENT);
        tax.setFont(new Font("Courier New", Font.BOLD, 24));
        total.setAlignmentX(Component.CENTER_ALIGNMENT);
        total.setFont(new Font("Courier New", Font.BOLD, 24));
        quantity.setAlignmentX(Component.CENTER_ALIGNMENT);
        quantity.setFont(new Font("Courier New", Font.BOLD, 24));


        centerPanel.add(subtotal);
        centerPanel.add(tax);
        centerPanel.add(total);
        centerPanel.add(quantity);
        centerPanel.setBackground(Color.RED);
        centerPanel.setBorder(new LineBorder(Color.DARK_GRAY, 3));
        mainContainer.add(centerPanel, BorderLayout.CENTER);

        setTitle("Register");
        setSize(1500, 1000);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void addLineItem(Item item) {
        JLabel temp = new JLabel("1\t" + item.getItemName() + "\t" + item.getItemValue());
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.setFont(new Font("Courier New", Font.BOLD, 12));
        leftReceiptPanel.add(temp);
        checkout.addItemToBasket(item);
        updateJLabelTrackers();
        leftReceiptPanel.revalidate();
        leftReceiptPanel.repaint();
    }

    public void addMultipleLineItem(Item item, int quantity) {
        double value = Double.parseDouble(item.getItemValue())*quantity;
        JLabel temp = new JLabel(quantity+"\t" + item.getItemName() + "\t" + value);
        temp.setAlignmentX(Component.CENTER_ALIGNMENT);
        temp.setFont(new Font("Courier New", Font.BOLD, 12));
        leftReceiptPanel.remove(leftReceiptPanel.getComponents().length - 1);
        leftReceiptPanel.add(temp);
        updateJLabelTrackers();
        leftReceiptPanel.revalidate();
        leftReceiptPanel.repaint();
    }

    public void removeLineItem() {
        Component[] componentList = leftReceiptPanel.getComponents();
        if (componentList.length > 2) {
            leftReceiptPanel.remove(componentList.length - 1);
            checkout.removeLastItemFromBasket();
            updateJLabelTrackers();
            leftReceiptPanel.revalidate();
            leftReceiptPanel.repaint();
        }
    }

    public void removeAllLeftPanelComponents() {
        Component[] componentList = leftReceiptPanel.getComponents();
        while (componentList.length > 2) {
            leftReceiptPanel.remove(componentList.length - 1);
            componentList = leftReceiptPanel.getComponents();
        }
        leftReceiptPanel.revalidate();
        leftReceiptPanel.repaint();
    }

    public void payByCredit() {
        removeAllLeftPanelComponents();
        checkout.payByCredit();
        updateJLabelTrackers();
    }

    public void payExactDollar() {
        removeAllLeftPanelComponents();
        checkout.payExactDollar();
        updateJLabelTrackers();
    }

    public void payNextDollar() {
        removeAllLeftPanelComponents();
        checkout.payNextDollar();
        updateJLabelTrackers();
    }

    public void voidTransaction() {
        removeAllLeftPanelComponents();
        checkout.voidTransaction();
        updateJLabelTrackers();
    }

    public void changeQuantity(int changeValue) {
        if(checkout.getItemQuantity()>0) {
            Item itemToChangeQuantity = checkout.returnLastItem();
            checkout.removeLastItemFromBasket();
            checkout.addMultiItemToBasket(itemToChangeQuantity,changeValue);
            addMultipleLineItem(itemToChangeQuantity,changeValue);
        }
    }

    public JButton createButton(Item item) {
        return new JButton("ADD\t1\t" + item.getItemName());
    }

    public void addBottomButtons() {
        for (int i = 0; i < 12; i++) {
            final int finalI = i;
            JButton temp = createButton(itemList.get(i));
            temp.addActionListener((ActionEvent event) -> addLineItem(itemList.get(finalI)));
            bottomQuickButtonPanel.add(temp);
        }
    }

    public void addRightButtons() {
        for (int i = 12; i < 18; i++) {
            final int finalI = i;
            JButton temp = createButton(itemList.get(i));
            temp.addActionListener((ActionEvent event) -> addLineItem(itemList.get(finalI)));
            rightQuickButtonPanel.add(temp);
        }
    }

    public void updateJLabelTrackers() {
        quantity.setText("QUANTITY:\t" + checkout.getItemQuantity());
        tax.setText("TAX:\t" + checkout.getTaxTotal());
        total.setText("TOTAL:\t" + checkout.getTotal());
        subtotal.setText("SUBTOTAL:\t" + checkout.getSubtotal());
    }
}
