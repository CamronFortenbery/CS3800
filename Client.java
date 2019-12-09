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
    
    public void start()
    {
        session = new ChatGUI();
        session.setClient(this);
        session.start();
        
    }
    
    public void setHostname(String newHostName)
    {
        hostname = newHostName;
        setupConnection();
    }
    
    public void setupConnection()
    {
        //Do what you need to set up the connection to the server
    }
    
    public void sendMessage(String messageToSend)
    {
        //input code to turn String into message and send it to server
    }
    
    public void getMessage(Message messageToDisplay)
    {
        //Input code here to change from Message to String
        //session.displayMessage(hostname, messageToDisplay);       
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