package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServerThread extends Thread {
    private final Socket SOCKET;
    private PrintWriter outputStream;
    private BufferedReader inputStream;

    private final long MESSAGE_COOLDOWN = 5000;
    private final int WARNING_THRESHOLD = 3;
    private long cooldownUntil;
    private int warningCount = 0;
    private String username;

    ServerThread(Socket socket) {
        SOCKET = socket;
    }

    @Override
    public void run() {
        try {
            inputStream = new BufferedReader(new InputStreamReader(SOCKET.getInputStream()));
            outputStream = new PrintWriter(SOCKET.getOutputStream(), true);

            outputStream.println("Welcome to the chat server! Press return anytime to disconnect. In the meantime, enter your username to join (cannot be empty):");
            String input;
            while (!SOCKET.isClosed()) {
                input = inputStream.readLine();
                if (username == null){
                    username = input;
                    if (username != null && !username.equals("")){
                        Server.printToAllClients(username + " joined.");
                    }
                } else {
                    if (System.currentTimeMillis() < cooldownUntil){
                        if(warnClient("you must wait at least " + MESSAGE_COOLDOWN / 1000 + " seconds after sending a message to send another.")){
                            break;
                        }

                        continue;
                    }
                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

        int clientsConnectedCount = Server.getServerThreads().size();
        System.out.println("Clients connected:" + clientsConnectedCount);
        if (clientsConnectedCount == 0){
            try{
                Socket socket = new Socket("localhost", Server.getPort());
                socket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    PrintWriter getOutputStream() {
        return outputStream;
    }
}