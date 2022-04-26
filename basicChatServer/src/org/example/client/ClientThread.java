package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ClientThread extends Thread {
    private final BufferedReader INPUT_STREAM;
    private final Socket SOCKET;

    ClientThread(Socket socket) throws IOException {
        SOCKET = socket;
        INPUT_STREAM = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            String messageToRead;
            while (true) {
                messageToRead = INPUT_STREAM.readLine();
                if (messageToRead == null) {
                    SOCKET.close();
                    break;
                }

                System.out.println(messageToRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            INPUT_STREAM.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}