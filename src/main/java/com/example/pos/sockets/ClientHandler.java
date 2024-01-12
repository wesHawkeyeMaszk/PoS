package com.example.pos.sockets;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;

@Component
@Getter
@Setter
class ClientHandler implements Runnable
{
    BufferedReader dis;
    PrintWriter dos;
    Socket s;
    Server server;
    boolean stopped = false;

    public ClientHandler(Server server, Socket s) {
        try {
            this.server = server;
            this.s = s;
            this.dis = new BufferedReader(new InputStreamReader((s.getInputStream())));
            this.dos = new PrintWriter(s.getOutputStream(), true);

            // create a new thread object
            Thread t = new Thread(this);
            // Invoking the start() method
            t.start();
        }catch ( Exception e) {
            System.out.println(e);
        }

    }

   public ClientHandler(){}

    @Override
    public void run()
    {
        while (!stopped)
        {
            try {
                char c = (char)dis.read();
                System.out.print(c);
            }
            catch (Exception e) {
                System.out.println(e);
                break;
            }
        }
        server.disconnect(this);

    }

    public void write(String message) throws IOException {
        dos.println(message);
    }

    public void stop(){
        this.stopped = true;
    }

}