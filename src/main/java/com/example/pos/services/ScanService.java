package com.example.pos.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


@Getter
@Setter
@Component
public class ScanService implements KeyEventDispatcher {

    private List<ScannerListener> scannerListeners;
    private StringBuffer stringBuffer;
    private Boolean buildingBuffer;

    public ScanService(){
        scannerListeners = new ArrayList<>();
        stringBuffer = new StringBuffer();
        buildingBuffer = false;
    }

    public void addScannerListener(ScannerListener scannerListener) {
        scannerListeners.add(scannerListener);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(!buildingBuffer){
            buildingBuffer = true;
            startBufferTimer();
        }
        if(e.getID() == KeyEvent.KEY_TYPED) {
            stringBuffer.append(e.getKeyChar());
        }
        return false;
    }


    public void useScan(String buffer){
        stringBuffer.setLength(0);
        buildingBuffer = false;
        if(!buffer.isEmpty()) {
            try {
                for(ScannerListener scannerListener: scannerListeners) {
                    scannerListener.onScan(buffer);
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
    }

    public void startBufferTimer(){
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        useScan(stringBuffer.toString().trim());
                    }
                },
                100
        );
    }
}
