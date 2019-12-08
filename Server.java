import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class Server {

    // port 48620-49150 is unassigned range
    static final int PORT_NUMBER = 48620;

/*
    public Server(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        this.outToClient = new ObjectOutputStream(this.connectionSocket.getOutputStream());
        this.inFromClient = new ObjectInputStream(this.connectionSocket.getInputStream());
        System.out.println("Server initiated");

    }
*/


    public static void main(String[] args) throws IOException {

        // ArrayList for keeping track of clients
        ArrayList<ClientHandler> clientList = new ArrayList<>();

        // index for clients
        int i = 0;

        // Initialize welcome socket
        ServerSocket welcomeSocket = new ServerSocket(PORT_NUMBER);
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

    Scanner scan = new Scanner(System.in);
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

        try {
            outToClient.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Receive username
        try {
            msg = (Message) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String username = msg.getMsg();

        // Reply with acknowledgement and
        msg.setMsg("Username received, thank you " + username);
        try {
            outToClient.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // notify rest of clients - help
        msg.setMsg(username + " has joined!");

        // Loop:
        // Wait and receive message from client
        try {
            msg = (Message) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Append client username to beginning of message and forward to other clients

        // Receive sign off message from client
        try {
            msg = (Message) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Reply with sign off
        msg.setMsg("Goodbye!");
        try {
            outToClient.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}