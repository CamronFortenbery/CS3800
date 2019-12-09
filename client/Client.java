package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    ChatGUI session;
    ObjectInputStream input;
    ObjectOutputStream output;
    String hostname;
    Socket clientSocket;
    Message msg;
    final int PORT_NUMBER = 48620;
    final String HOSTNAME = "localhost";

    public void start() {
        session = new ChatGUI();
        session.setClient(this);
        session.start();
        msg = new Message();
    }

    public void setHostname(String newHostName) {
        hostname = newHostName;
        setupConnection();
    }

    public void setupConnection() {
        //Do what you need to set up the connection to the server
        try {
            clientSocket = new Socket("localhost", 48620);
        } catch (IOException e) {
            System.out.println("Invalid Host!");
        }
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Output or input stream error!");
        }

        // Send username to server
        sendMessage(hostname);

        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                // Create and set message object
                Message incomingMsg = new Message();

                while (true) {
                    // Receive message
                    try {
                        incomingMsg = (Message) input.readObject();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    // Interrupt current thread if receive SIGN_OFF from server
                    if (incomingMsg.getMsgType() == Message.SIGN_OFF)
                        Thread.currentThread().interrupt();

                    // Read message from server
                    getMessage(incomingMsg);

                }

            }
        });
        readMessage.start();
    }

    public void sendMessage(String messageToSend) {
        //input code to turn String into message and send it to server
        Message outgoingMsg = new Message();

        // Check for exit
        if (messageToSend.equals("<" + hostname + "> " + ".")) {
            outgoingMsg.setMsgType(Message.SIGN_OFF);
        } else {
            outgoingMsg.setMsgType(Message.CHAT);
            outgoingMsg.setMsg(messageToSend);
        }
        try {
            output.writeObject(outgoingMsg);
            output.flush();
            output.reset();
        } catch (Exception e) {
            System.out.println("Problem sending message!!");
        }


    }

    public void getMessage(Message messageToDisplay) {
        //Input code here to change from Message to String
        session.displayMessage(messageToDisplay.getMsg());
    }

    public void updateUserList(Message newUser) {
        //Input code here to change from Message to String
        //session.addNewUser(newUser);

    }


    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}