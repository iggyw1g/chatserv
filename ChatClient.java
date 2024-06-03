/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatserver;

/**
 *
 * @author alisa
 */
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_ADDRESS = "10.152.154.166"; //local host is IP address or where server is
    private static final int SERVER_PORT = 12345; //port is basically where the info comes in from network

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);//creating new server using port + IP address
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));//getting input from server
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);//output from user
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) { //InputStreamReader reads bytes and decodes into characters

            // Handle server messages
            new Thread(() -> {//threads are each incoming connection
                String serverMessage;
                try {
                    while ((serverMessage = in.readLine()) != null) {
                        System.out.println(serverMessage);//reading input and printing from server
                    }
                } catch (IOException e) {
                    e.printStackTrace();//if its null then this diagnoses exception and then prints
                }
            }).start(); 

            // Handle user input
            String userInput;
            while ((userInput = stdIn.readLine()) != null) { 
                out.println(userInput);//printing each message
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}