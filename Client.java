import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        final int PORT_NUMBER = 48620;
        final String HOSTNAME = "localhost";

        // Connect to server using TCP sockets
        // assume hostname and port # are already known by client
        Socket clientSocket = new Socket(HOSTNAME, PORT_NUMBER);
        System.out.println("Connected to server");

        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());

        // Instantiate message obj
        Message msg = new Message();

        // Instantiate Scanner
        Scanner scan = new Scanner(System.in);

        // Receive welcome message
        // Receive Message 0
        msg = (Message) inFromServer.readObject();
        System.out.println(msg.getMsg());
        String username = scan.nextLine();

        // Send username to server
        msg.setMsg(username);
        // Send Message 1
        outToServer.writeObject(msg);
        outToServer.flush();
        outToServer.reset();

        // Receive acknowledgement from server
        // Should probably add some sort of verification here
        // Receive Message 2
        msg = (Message) inFromServer.readObject();
        System.out.println(msg.getMsg());

        // Ask user to enter message to send to other clients
        System.out.println("You can now send messages to other clients!");

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                // Create and set message object
                Message outgoingMsg = new Message();
                outgoingMsg.setMsgType(Message.CHAT);

                while (true) {
                    // Receive message from user
                    outgoingMsg.setMsg(scan.nextLine());

                    // Check for quit
                    if (outgoingMsg.getMsg().equals(".")) {
                        // Exit threads and start sign off procedure
                        Thread.currentThread().interrupt();

                    }

                    // Send message to server
                    try {
                        outToServer.writeObject(outgoingMsg);
                        outToServer.flush();
                        outToServer.reset();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        // readMessage thread
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                // Create and set message object
                Message incomingMsg = new Message();
                incomingMsg.setMsgType(Message.CHAT);

                while (true) {
                    // Interrupt current thread if sendMessage thread is interrupted
                    if (sendMessage.isInterrupted())
                        Thread.currentThread().interrupt();

                    // Read message from server
                    try {
                        incomingMsg = (Message) inFromServer.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Display to user
                    System.out.println(incomingMsg.getMsg());
                }

            }
        });

        // Start threads
        sendMessage.start();
        readMessage.start();

        // Exit:
        // Send sign off message to server
        msg.setMsg("SIGN_OFF");
        msg.setMsgType(Message.SIGN_OFF);
        outToServer.writeObject(msg);
        outToServer.flush();
        outToServer.reset();

        // Leave group
        // Accomplished by clientHandler in Server.java

        // Wait for confirmation from server
        msg = (Message) inFromServer.readObject();
        System.out.println(msg.getMsg());
        // add verification here?

        // Close resources
        inFromServer.close();
        outToServer.close();
        clientSocket.close();
        scan.close();
    }
}