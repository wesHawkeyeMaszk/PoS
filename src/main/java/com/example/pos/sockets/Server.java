package com.example.pos.sockets;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
@Component
@Getter
@Setter
public class Server {
    static List<ClientHandler> clients;

    public Server(){
        clients = new ArrayList<>();
    }
    public void start(int port) throws IOException {
        // server is listening on port 5056
        ServerSocket serverSocket = new ServerSocket(port);
        // running infinite loop for getting
        // client request
        while (true)
        {
            Socket s = null;
            try
            {
                // socket object to receive incoming client requests
                s = serverSocket.accept();
                System.out.println("A new client is connected : " + s);
                connect(s);
            }
            catch (Exception e){
                s.close();
                e.printStackTrace();
            }
        }
    }

    public void broadcast(final String message){
        for(ClientHandler client : clients){
            try
            {
                client.write(message);
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public void connect(Socket socket) throws IOException {
        ClientHandler client = new ClientHandler(this, socket);
        clients.add(client);
    }

    public void disconnect(ClientHandler client){
        clients.remove(client);
        client.stop();
    }
}
