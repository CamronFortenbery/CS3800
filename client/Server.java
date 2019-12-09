package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    // port 48620-49150 is unassigned range
    static final int PORT_NUMBER = 48620;

    // ArrayList for keeping track of clients
    static ArrayList<ClientHandler> clientList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        // index for clients
        int i = 0;

        // Initialize welcome socket
        ServerSocket welcomeSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("Server initiated");
        Socket connectionSocket;

        // Repeatedly accept new connections
        while (true) {
            // Accept connection
            connectionSocket = welcomeSocket.accept();
            System.out.println("Client connected: " + connectionSocket);

            // Get I/O streams for handler class
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());

            // Create handler object
            ClientHandler client = new ClientHandler(connectionSocket, "client " + i++, outToClient, inFromClient);

            // Spawn thread
            Thread clientThread = new Thread(client);

            // Add client to list
            clientList.add(client);

            // Start thread
            clientThread.start();
        }

    }

}

class ClientHandler implements Runnable {

    private String username;
    final ObjectOutputStream outToClient;
    final ObjectInputStream inFromClient;
    Socket connectionSocket;

    public ClientHandler(Socket connectionSocket, String username,
                         ObjectOutputStream outToClient, ObjectInputStream inFromClient) {
        this.connectionSocket = connectionSocket;
        this.outToClient = outToClient;
        this.inFromClient = inFromClient;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {

        // Send welcome message
        Message msg = new Message();
        msg.setMsg("Welcome!");
        // msg.setMsgType(Message.SIGN_IN);

        try {
            // Send message 0
            outToClient.writeObject(msg);
            outToClient.flush();
            outToClient.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Receive username
        try {
            msg = (Message) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        username = msg.getMsg();

        // notify rest of clients
        msg.setMsg(username + " has joined!");
        msg.setMsgType(Message.CHAT);

        sendMessage(msg);

        // Loop:
        while (true) {

            // Wait and receive message from client
            try {
                msg = (Message) inFromClient.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            // If server receives sign off message
            if (msg.getMsgType() == Message.SIGN_OFF) {
                // Remove client from list
                Server.clientList.remove(this);

                // Reply with sign off message
                msg.setMsg("Goodbye!");
                msg.setMsgType(Message.SIGN_OFF);
                try {
                    outToClient.writeObject(msg);
                    outToClient.flush();
                    outToClient.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Tell other users
                msg.setMsg(this.username + " has signed off");
                sendMessage(msg);

                // Exit loop
                break;
            }
            // Send message to other clients
            sendMessage(msg);
        }
        // End of loop

        // Close resources
        try {
            this.outToClient.close();
            this.inFromClient.close();
            this.connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendMessage(Message msg) {
        for (ClientHandler client : Server.clientList
        ) {
            try {
                client.outToClient.writeObject(msg);
                client.outToClient.flush();
                client.outToClient.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}