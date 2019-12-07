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
        msg = (Message) inFromServer.readObject();


        // Ask user to enter message to send to other clients

        // Send message every time user hits 'enter'

        // Receive messages from server and display to user

        // If user enters '.' begin exit procedure

        // Exit:
        // Send sign off message to server

        // Leave group

        // Wait for confirmation from server


        // Copy pasted stuff, havent touched it
        InetAddress address = InetAddress.getLocalHost();
        Socket s1 = null;
        String line = null;
        BufferedReader br = null;
        BufferedReader is = null;
        PrintWriter os = null;

        try {
            s1 = new Socket(address, 5000);
            br = new BufferedReader(new InputStreamReader(System.in));
            is = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            os = new PrintWriter(s1.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("You can now start sending messages to server!");

        String response = null;
        try {
            line = br.readLine();
            while (line.compareTo("QUIT") != 0) {
                os.println(line);
                os.flush();
                response = is.readLine();
                System.out.println("Response from Server : " + response);
                line = br.readLine();

            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            is.close();
            os.close();
            br.close();
            s1.close();
        }

    }
}