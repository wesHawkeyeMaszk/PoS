package com.example.pos.services;


import org.springframework.stereotype.Component;

@Component
public interface ScannerListener {
    public void onScan(String scan);
}
