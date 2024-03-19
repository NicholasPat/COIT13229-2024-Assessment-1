package fitnessclubprogram.assessment1;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author linke
 */
public class TCPServer {
    //Will become main method in the final 
    //public static void 
    public static void serverMain() { 
        try { 
            int serverPort = 8864 ; //Change port to required in thing 
            ServerSocket listenSocket = new ServerSocket(serverPort) ; 
            while (true) { 
                Socket clientSocket = listenSocket.accept() ; 
                Connection c = new Connection(clientSocket) ; 
            } 
        } catch (IOException e) { 
            System.out.println("Listen socket: " + e.getMessage()) ; 
        }
    }
}

class Connection extends Thread { 
    ObjectInputStream in ; 
    ObjectOutputStream out ; 
    Socket clientSocket ; 
    int count = 0 ; 
    
    public Connection(Socket aClientSocket) { 
        try { 
            clientSocket = aClientSocket ; 
            in = new ObjectInputStream(clientSocket.getInputStream()) ; 
            out = new ObjectOutputStream(clientSocket.getOutputStream()) ; 
            this.start() ; 
        } catch (IOException e) { 
            System.out.println("Conncetion: " + e.getMessage()) ; 
        }
    }
    
    public void run() { 
        try { 
            Member member = (Member)in.readObject() ; 
            System.out.println("Receiving data from client: " + (count++)) ; 
            System.out.println(member.toString()) ;
            //Server output to client 
            
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
	} catch(IOException e) {System.out.println("readline:"+e.getMessage());
	} catch(ClassNotFoundException ex){
            ex.printStackTrace();
	}finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
    }
    
}