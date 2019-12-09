package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client 
{
    ChatGUI session;
    ObjectInputStream input;
    ObjectOutputStream output;
    String hostname;
    Socket clientSocket;
    Message msg;
    final int PORT_NUMBER = 48620;
    final String HOSTNAME = "localhost";
    
    public void start()
    {
        session = new ChatGUI();
        session.setClient(this);
        session.start();
        msg = new Message();
        
        
        
    }
    
    public void setHostname(String newHostName)
    {
        hostname = newHostName;
        setupConnection();
    }
    
    public void setupConnection()
    {
        //Do what you need to set up the connection to the server
        try {
            clientSocket = new Socket("localhost", 48620);
        } catch (IOException e) {
            System.out.println("Invalid Host!");
        }
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        }catch (IOException e) {
            System.out.println("Output or input stream error!");
        }

    }
    
    public void sendMessage(String messageToSend)
    {
        //input code to turn String into message and send it to server
        Message outgoingMsg = new Message();
        outgoingMsg.setMsgType(Message.CHAT);
        outgoingMsg.setMsg(messageToSend);
        try {
            output.writeObject(outgoingMsg);
        } catch (Exception e) {
            System.out.println("Problem sending message!!");
        }
        

    }
    
    public void getMessage(Message messageToDisplay)
    {
        //Input code here to change from Message to String
        //session.displayMessage(hostname, messageToDisplay);  

    	String newString = String.valueOf(messageToDisplay);
        session.displayMessage(newString);

    }
    
    public void updateUserList(Message newUser)
    {
       //Input code here to change from Message to String
       //session.addNewUser(newUser);

    }
    
    
    

    public static void main(String[] args)
    {
        Client client = new Client();
        client.start();
    }
}