package org.example.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private Client() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter server address: ");
        String address = scanner.nextLine();

        int port;
        try {
            System.out.print("Enter the server port: ");
            port = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Not a valid integer!");
            System.exit(1);
            return;
        }

        try (Socket socket = new Socket(address, port)) {
            ClientThread clientThread = new ClientThread(socket);
            clientThread.start();

            PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);

            String userInput;
            while (!socket.isClosed()) {
                userInput = scanner.nextLine();
                outputStream.println(userInput);
            }
        } catch (UnknownHostException unknownHostException) {
            System.err.println("Not a valid host to connect to!");
            unknownHostException.printStackTrace();
            scanner.close();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            scanner.close();
            System.exit(1);
        }

        scanner.close();
    }
}
