/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg4enlinea;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rodri
 */
public class Server {
    private Socket socket;
    private ServerSocket serverSocket;
    private ExecutorService service;
    private List<matriz> clientList;

    public Server() {
        try {
            clientList = new ArrayList<>();
            service = Executors.newCachedThreadPool();
            serverSocket = new ServerSocket(7521);
            accept();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void accept() {
        try {
            while (true) {
                socket = serverSocket.accept();
                matriz clientThread = new matriz(socket,clientList);
                clientList.add(clientThread);
                service.submit(clientThread);
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
