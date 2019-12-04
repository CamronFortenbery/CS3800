import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

public static void main(String args[]) throws IOException{


    InetAddress address=InetAddress.getLocalHost();
    Socket s1=null;
    String line=null;
    BufferedReader br=null;
    BufferedReader is=null;
    PrintWriter os=null;

    try {
        s1=new Socket(address, 5000); 
        br= new BufferedReader(new InputStreamReader(System.in));
        is=new BufferedReader(new InputStreamReader(s1.getInputStream()));
        os= new PrintWriter(s1.getOutputStream());
    }
    catch (IOException e){
        e.printStackTrace();
        System.err.print("IO Exception");
    }

    System.out.println("You can now start sending messages to server!");

    String response=null;
    try{
        line=br.readLine(); 
        while(line.compareTo("QUIT")!=0){
                os.println(line);
                os.flush();
                response=is.readLine();
                System.out.println("Response from Server : "+response);
                line=br.readLine();

            }



    }
    catch(IOException e){
        e.printStackTrace();
    }
    finally{

        is.close();
        os.close();
        br.close();
        s1.close();
    }

    }
}