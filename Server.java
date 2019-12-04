import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
public static void main(String args[]){


    Socket s=null;
    ServerSocket server2=null;
    try{
        server2 = new ServerSocket(5000); 

    }
    catch(IOException e){
    e.printStackTrace();
    System.out.println("Server error");

    }

    while(true){
        try{
            s = server2.accept();
            System.out.println("connection Established");
            ServerThread st = new ServerThread(s);
            st.start();

        }

    catch(Exception e){
        e.printStackTrace();

        }
    }

}

}

class ServerThread extends Thread{  

    String line=null;
    BufferedReader  is = null;
    PrintWriter os=null;
    Socket s=null;

    public ServerThread(Socket s){
        this.s=s;
    }

    public void run() {
    try{
        is= new BufferedReader(new InputStreamReader(s.getInputStream()));
        os=new PrintWriter(s.getOutputStream());

    }catch(IOException e){
        System.out.println("IO error in server thread");
    }

    try {
        line=is.readLine();
        while(line.compareTo("QUIT")!=0){

            os.println(line);
            os.flush();
            System.out.println("Response to Client:  "+line);
            line=is.readLine();
        }   
    } catch (IOException e) {

        line=this.getName(); 
    }
    catch(NullPointerException e){
        line=this.getName(); 
      
    }

    finally{    
    try{
        System.out.println("Connection Closing..");
        if (is!=null){
            is.close(); 
            
        }

        if(os!=null){
            os.close();
        }
        if (s!=null){
            s.close();
        }

        }
    catch(IOException ie){
        System.out.println("Socket Close Error");
    }
    }//end finally
    }
}