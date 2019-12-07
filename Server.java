import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable {

    // port 48620-49150 is unassigned range
    static final int PORT_NUMBER = 48620;
    Socket connectionSocket = null;
    ObjectOutputStream outToClient = null;
    ObjectInputStream inFromClient = null;

    public Server(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        this.outToClient = new ObjectOutputStream(this.connectionSocket.getOutputStream());
        this.inFromClient = new ObjectInputStream(this.connectionSocket.getInputStream());
        System.out.println("Server initiated");

    }


    public static void main(String args[]) throws IOException {

        // Initialize welcome socket
        ServerSocket welcomeSocket = new ServerSocket(PORT_NUMBER);

        // Accept connection and spawn thread
        Server server = new Server(welcomeSocket.accept());
        Thread chatThread = new Thread(server);
        chatThread.start();

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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Append client username to beginning of message and forward to other clients

        // Receive sign off message from client
        try {
            msg = (Message) inFromClient.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Reply with sign off
        msg.setMsg("Goodbye!");
        try {
            outToClient.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // below this point is copy pasted and not yet edited
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 /*       try {
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
            line = is.readLine();
            while (line.compareTo("QUIT") != 0) {

                os.println(line);
                os.flush();
                System.out.println("Response to Client:  " + line);
                line = is.readLine();
            }
        } catch (IOException e) {

            line = this.getName();
        } catch (NullPointerException e) {
            line = this.getName();

        } finally {
            try {
                System.out.println("Connection Closing..");
                if (is != null) {
                    is.close();

                }

                if (os != null) {
                    os.close();
                }
                if (s != null) {
                    s.close();
                }

            } catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }//end finally */
    }
}