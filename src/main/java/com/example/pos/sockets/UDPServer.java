package com.example.pos.sockets;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer extends Thread{

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    public UDPServer(int port) {
        try {
            socket = new DatagramSocket(4445);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run() {
        running = true;
        try {

            while (running) {
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received
                        = new String(packet.getData(), 0, packet.getLength());

                if (received.equals("end")) {
                    running = false;
                    continue;
                }
                socket.send(packet);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        socket.close();
    }


    public static void main(String... args) {
        UDPServer udpServer = new UDPServer(4000);
        udpServer.start();
    }
}