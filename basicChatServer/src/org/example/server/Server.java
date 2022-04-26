package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    private static final String QUIT_COMMAND = "quit";
    private static List<ServerThread> serverThreads;
    private static int port;

    private Server() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter the port to run the server on: ");
            port = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Not a valid integer!");
            System.exit(1);
        }
        scanner.close();

        serverThreads = new ArrayList<>();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running. See computer network settings for your IP address.");
            boolean start = true;
            Socket socket;
            ServerThread serverThread;
            while (true) {
                socket = serverSocket.accept();
                if (serverThreads.size() == 0 && !start) {
                    socket.close();
                    break;
                }
                start = false;

                serverThread = new ServerThread(socket);
                serverThreads.add(serverThread);
                serverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getQuitCommand() {
        return QUIT_COMMAND;
    }

    static List<ServerThread> getServerThreads() {
        return serverThreads;
    }

    public static int getPort() {
        return port;
    }

    static void printToAllClients(String message) {
        System.out.println(message);
        for (ServerThread serverThread : serverThreads) {
            serverThread.getOutputStream().println(message);
        }
    }
}