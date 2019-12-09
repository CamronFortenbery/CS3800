package client;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import java.util.ArrayList;

public class ChatGUI 
{
    JFrame mainFrame;
    String currentUser;
    ArrayList users = new ArrayList();
    JTextArea chatArea;
    JTextArea userDisplayArea;
    Client client;
        
    //Call this after creating a GUI for a client. The main method is only used
    //when this class isn't hooked up to the client network. This method sets 
    //the gui to run until it is closed. The window is created and then calls
    // userLoginWindow method to create the screen to ask for a username.
    public void start()
    {
        SwingUtilities.invokeLater(new Runnable() 
        {
            @Override
            public void run() 
            {
                try 
                {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                
                
                mainFrame = new JFrame("Chat V 3800");
                mainFrame.setSize(800, 600);
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setResizable(false);
                userLoginWindow();
            }
        });
    }
    
    //A window is made that displays a textbox where the user can enter their
    //username. The username can be entered by typing in the textbox and hitting
    //The enter key on the keyboard. Once they enter a viable username that
    //username is set as currentUser and is added to the users ArrayList.
    //After the client and server is done, it should instead save our username
    //and send the username to the server. After the server recieves it and
    //adds it to its users List, it should give us a copy of its userList.
    public void userLoginWindow()
    {
        JPanel loginWindow = new JPanel(new GridBagLayout());
        
        GridBagConstraints G = new GridBagConstraints();
        
        
        
        JTextField usernameInput = new JTextField(15);
        
        JLabel usernameScreenLabel = new JLabel("Please enter a Username");
        usernameScreenLabel.setFont(new Font("Courier New", Font.ITALIC, 20));
        
        G.gridx = 1;
        G.gridy = 0;
        loginWindow.add(usernameInput, G);
        
        G.gridx = 1;
        G.gridy = 1;
        loginWindow.add(usernameScreenLabel, G);
        
        mainFrame.add(loginWindow);
        mainFrame.setVisible(true);
        
        usernameInput.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter"); 
        usernameInput.getActionMap().put("enter", new AbstractAction() 
        {     
            @Override    
            public void actionPerformed(ActionEvent e) 
            {
                if(usernameInput.getText().length() < 1)
                {
                    JOptionPane.showMessageDialog(loginWindow,
                            "There was no username Entered", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                if(usernameInput.getText().length() > 15)
                {
                    JOptionPane.showMessageDialog(loginWindow,
                            "That username is too long, please choose one that"
                                    + " is less then 15 symbols", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    currentUser = usernameInput.getText();
                    
                    //Change this to send the currentUser to the server
                    //and then have the server call the changeUserList method
                    users.add(currentUser);
                    client.setHostname(currentUser);
                    
                    
                    //This window becomes invisible and the chat window will be
                    //called and set visible to the user.
                    loginWindow.setVisible(false);
                    chatWindow();
                    
                }

            }    
        });    
    }
    
    //A window is displayed that has an area to display current users to the 
    //left, a main scrollable text area to the right of the current user area,
    //And an area to enter text from at the bottom. The text can be entered
    //using the enter key. Once the Server/Client system is set up make the 
    //change listed below in the action preformed section.
    public void chatWindow()
    {
        JPanel chatWindow = new JPanel(new BorderLayout());
        chatWindow.setBackground(Color.BLACK);
        
       
        
        JPanel messagePanel = new JPanel(new BorderLayout());
        
        JTextField messageInput = new JTextField(30);
        
        JLabel enterTextLabel = 
                new JLabel("Enter Message as " + currentUser + ":");
        
        enterTextLabel.setSize(50, 20);
        
        
        messagePanel.add(messageInput, BorderLayout.CENTER);
        messagePanel.add(enterTextLabel, BorderLayout.LINE_START);
        
        userDisplayArea = new JTextArea();
        userDisplayArea.setEditable(false);
        userDisplayArea.setFont(new Font("Serif", Font.BOLD, 15));
        userDisplayArea.setLineWrap(true);
        userDisplayArea.setSize(120, 50);
        
        
        
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Serif", Font.PLAIN, 15));
        chatArea.setLineWrap(true);
        chatArea.setSize(670, 50);
        
         JScrollPane scroll = new JScrollPane(chatArea);
        
        chatWindow.add(scroll, BorderLayout.LINE_END);
        chatWindow.add(userDisplayArea, BorderLayout.LINE_START);
        chatWindow.add(messagePanel, BorderLayout.PAGE_END);
        
        
        
        mainFrame.add(chatWindow);
        messagePanel.setVisible(true);
        chatWindow.setVisible(true);
        
        displayUsers();
        displayMessage("<SYSTEM_MESSAGE> YOU HAVE ENTERED THE CHAT ROOM");
        
        messageInput.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enter"); 
        messageInput.getActionMap().put("enter", new AbstractAction() 
        {     
            @Override    
            public void actionPerformed(ActionEvent e) 
            {
                //Once the Server/Client system is set up change this to send 
                //the message to the server, along with our currentUsername.
                //Then have the server call displayMessage() of all clients,
                //while sending them the message that it recieved.
                //displayMessage(currentUser, messageInput.getText());
                sendMessage(currentUser, messageInput.getText());
                
                
                //Keep this After the changes listed above, don't call it from
                //Every client or it will delete messages that are being written
                //in all clients whether they were sent or not.
                messageInput.setText("");
                    
           
            }
        });
    }
    
    public void sendMessage(String user, String message)
    {
      String messageToSend =("<" + user + "> " + message);
      client.sendMessage(messageToSend);
    }
    //Displays the input String onto the screen
    public void displayMessage(String message)
    {
        chatArea.append(message + "\n");
    }
    
    //Displays the current version of the User List on the userDisplayArea
    public void displayUsers()
    {        
        userDisplayArea.setText("");
        userDisplayArea.append("Online Users:\n");
        for(int x = 0; x < users.size(); x++)
        {
            userDisplayArea.append(users.get(x).toString() + "\n");
        }
    }
    
    //This will be used the by server/client when updating all users ArrayLists
    public void changeUserList(ArrayList userList)
    {
        users = userList;
        displayUsers();
    }
    
    
    //This is mostly used for testing, use changeUserList instead is calling
    //from the server
    public void addNewUser(String newUsername)
    {
        users.add(newUsername);
        displayUsers();
    }
    
    public void setClient(Client newClient)
    {
        this.client = newClient;
    }
}
