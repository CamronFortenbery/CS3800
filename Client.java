import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String args[]) throws IOException, ClassNotFoundException {

        final int PORT_NUMBER = 48620;
        final String HOSTNAME = "localhost";

        // Connect to server using TCP sockets
        // assume hostname and port # are already known by client
        Socket clientSocket = new Socket(HOSTNAME, PORT_NUMBER);
        System.out.println("Connected to server");

        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

        // Prompt user to enter username
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter a username");
        String username = scan.nextLine();

        // Send username to server
        Message msg = new Message();
        msg.setMsg(username);
        outToServer.writeObject(msg);

        // Receive acknowledgement from server
        // Should probably add some sort of verification here
        msg = (Message) inFromServer.readObject();
        System.out.println(msg.getMsg());

        // Ask user to enter message to send to other clients
        System.out.println("You can now send messages to other clients!");

        String reply;
        while (true) {
            // Loop:
            // Send message every time user hits 'enter'
            reply = scan.nextLine();
            msg.setMsg(reply);
            outToServer.writeObject(msg);

            // Receive messages from server and display to user
            // Not sure how to poll this, help
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            msg = (Message) inFromServer.readObject();
            System.out.println(msg);
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

            // If user enters '.' begin exit procedure
            if (msg.getMsg().equals("."))
                break;
        }

        // Exit:
        // Send sign off message to server
        msg.setMsg("SIGN_OFF");
        outToServer.writeObject(msg);

        // Leave group
        // Idk what this means besides disconnect from server

        // Wait for confirmation from server
        msg = (Message) inFromServer.readObject();
        // add verification here?

        clientSocket.close();
        scan.close();
    }
}