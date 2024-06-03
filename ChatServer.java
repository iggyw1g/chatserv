/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatserver;
 
/**
 *
 * @author alisa
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345; // connects to network and listens
    private static Map<String, ClientHandler> clientHandlers = Collections.synchronizedMap(new HashMap<>());//creating array list for the clients

    public static void main(String[] args) {
        System.out.println("Chat server started...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();//accepting the clients
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket; 
        private PrintWriter out;
        private BufferedReader in;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket; 
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//reading bytes to the socket
                out = new PrintWriter(socket.getOutputStream(), true); //writing bytes to the socket

                out.println("Enter your username:");
                username = in.readLine();
                if (username == null || username.trim().isEmpty()) {
                    out.println("Invalid username. Connection closing.");
                    socket.close();
                    return;
                }

                synchronized (clientHandlers) {
                    if (clientHandlers.containsKey(username)) {
                        out.println("Username already taken. Connection closing.");
                        socket.close();
                        return;
                    }
                    clientHandlers.put(username, this);
                    System.out.println(username + " connected.");
                    out.println("Welcome " + username + "!");
                }

                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("@")) { //private messaging 
                        int spaceIndex = message.indexOf(' ');
                        if (spaceIndex != -1) {
                            String targetUser = message.substring(1, spaceIndex);
                            String privateMessage = message.substring(spaceIndex + 1);
                            sendMessageToUser(targetUser, privateMessage);
                        } else {
                            out.println("Invalid message format. Use @username message.");
                        }
                    } else {
                        broadcast(username + ": " + message);//broadcasting it to that person 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        clientHandlers.remove(username);
                        System.out.println(username + " disconnected.");
                    }
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendMessageToUser(String targetUser, String message) {
            ClientHandler targetHandler = clientHandlers.get(targetUser);
            if (targetHandler != null) {
                targetHandler.out.println("[Private] " + username + ": " + message);//sending private messaqe 
            } else {
                out.println("User " + targetUser + " not found.");
            }
        }

        private void broadcast(String message) {
            synchronized (clientHandlers) {
                for (ClientHandler handler : clientHandlers.values()) {
                    handler.out.println(message);
                }
            }
        }
    }
}
