package fitnessclubprogram.assessment1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimerTask;

/**
 * @author Nicholas Paterno 12188564
 * TcpServer.java 
 * This class is what the TcpClient is communicating with. It creates an active socket and listens for connection 
 * It takes the Member text string sent by the client and then processes it into the memberlist.txt file 
 * Every 2 seconds, the timer task activates which will take the memberlist.txt file and serialise all the 
 * member details listed inside 
 */
public class TcpServer {    
    
    //Starts scheduled task, tries to create socket, accepts and creates connections 
    public static void main(String args[]) { 
        //Code provided by Uni, basically just invoke the class at 2000ms intervals 
        int interval = 2000; //Print to file every 2 seconds
        java.util.Timer tm = new java.util.Timer(); 
        tm.schedule(new WriteObjectToFile(), interval, interval);
        
        //Try to create the server connection and keep it open 
        try { 
            int serverPort = 8864 ; //ID ends in -64
            int i = 0 ; 
            ServerSocket listenSocket = new ServerSocket(serverPort) ; 
            
            //While socket is active, accept listen and create a connection (thread) 
            while (true) { 
                Socket clientSocket = listenSocket.accept() ; 
                Connection c = new Connection(clientSocket, i++) ; 
                System.out.printf("\nServer waiting on: %d for client from %d ",
                 listenSocket.getLocalPort(), clientSocket.getPort() );
            } 
        } catch (IOException e) { 
            System.out.println("Listen socket: " + e.getMessage()) ; 
        }
    }
}

/** 
 * @author Nicholas Paterno 12188564
 * Connection Class 
 * This class extends Thread which means multiple instances of TcpClient can connect to it at a time and it can handle all those connections 
 * Here is where the individual members are added to file 
 */
class Connection extends Thread { 
    DataInputStream in ; 
    DataOutputStream out ; 
    Socket clientSocket ; 
    int count ; 
    
    //Constructor for each connection made to the server, and it can do multiple and works as intended 
    //int i is taken for giving each thread a count 
    public Connection(Socket aClientSocket, int i) {
        try { 
            count = i ; 
            clientSocket = aClientSocket ; 
            
            //Creates streams for input and output. Then starts those streams and waits for connections 
            in = new DataInputStream(clientSocket.getInputStream()) ; 
            out = new DataOutputStream(clientSocket.getOutputStream()) ; 
            this.start() ; //Initiate the thread 
        } catch (IOException e) { System.out.println("Conncetion: " + e.getMessage()) ; }
    }
    
    //The actual input done by the client, so takes member object given, calls the write to file method, then writes to file 
    public void run() { 
        try { 
            //Text input from the client 
            String data = in.readUTF() ; 
            
            //Process the count and send back string with the count 
            int currentMemberCount = getCountFromString(data) ; 
            out.writeUTF("Server received: Save data of member number: " + currentMemberCount) ;
            
            //Appendix 1
            System.out.println("Receiving data from client of current Thread count: " + count) ; 
            updateTextFile(data) ; //Update file 
        } catch(EOFException e){System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {System.out.println("readline:"+e.getMessage());
        }finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
    }
    
    //This makes the .txt file from the entries given which is to be made into an object via createFile() method 
    public static void updateTextFile(String data) {
        String stringA = splitString(data) ; 
        String fileName = "memberlist.txt" ; 
        
        //Try and check for file, and if there just append to the end and don't recreate the whole thing by iterating over an array and adding them all 
        //The identifying string is put in as it makes it easier later to cut the string using that identifier, I feel ":" wouldn't have been good as could be used in some contexts 
        try { 
            //Printwriter: Writes to the nested FileWriter which writes to the file 
            FileWriter fw = new FileWriter(fileName, true) ; //"true" tag makes it appendable 
            PrintWriter pw = new PrintWriter(fw) ; 
            pw.write(stringA + "\n") ; 
            
            //Make sure to close or else it keeps trying and bloats the file. Made that mistake before 
            pw.close() ; 
            fw.close() ; 
        } catch (IOException e) { 
            System.out.println(fileName + ": doesn't exist") ; }
    }
    
    //This is used to get the count of the member number from the string given by the TcpClient 
    //Inefficient and hacky but should get the job done. Discards member object information 
    private static int getCountFromString(String text) { 
        int counter = 0 ; 
        String[] brokenString = text.split("::") ; 
        
        try { 
            counter = Integer.parseInt(brokenString[4]) ; 
        } catch(NumberFormatException e) { 
            //Should never error, unless manually editing the .tct file 
            System.out.println("Current array variable considered: " + brokenString[4]) ; 
        }
        return counter ; 
    }
    
    //Inelegent, but removes the "::x" from the counter added and returns rest of string 
    private static String splitString(String text) {
        String[] split = text.split("::") ; 
        String stringAA = split[0] + "::" + split[1] + "::" +split[2] + "::" + split[3]; 
        return stringAA ; 
    }
}

/** 
 * @author Nicholas Paterno 12188564
 * WriteObjectToFile Class 
 * Each time the timer ticks 2 seconds, do this, which is just make an array by reading the file in the system and then serialise the objects into an object file 
 * If no file exists, just doesn't do anything and simply waits for the file to be populated 
 */
class WriteObjectToFile extends TimerTask { 
    @Override
    public void run() { 
        //Definitions 
        String fileName = "memberlistObject" ; 
        ArrayList<Member> completeMemberList = readTextFile() ;  
        FileOutputStream fos ; ObjectOutputStream out ; 
        
        //Try to serialise the objects and catch the exception. Take the whole array and it just works being writing to file. Don't need to iterate over the array 
        try { 
            fos = new FileOutputStream(fileName) ;  
            out = new ObjectOutputStream(fos) ; 
            
            //This makes the objects serialise as an ArrayList<Member>. Meaning it will cast as an ArrayList<Member> when deserialised, not Member 
            out.writeObject(completeMemberList) ; 
            out.close() ; 
            completeMemberList.clear() ; 
        } catch (IOException ex) { ex.printStackTrace() ; }
    }
    
    //Readng the txt file and then making the memberobjectList file. Basically read the string line by line, chop it up into an array and remove the "::" identifier 
    private static ArrayList<Member> readTextFile() { 
        //Definitions 
        File file = new File("memberlist.txt") ; 
        ArrayList<Member> fullMemberList = new ArrayList<>() ; 
        String stringText ; 
        String[] brokenString ; 
        
        //Attempt to read the file, if successful read into the array line by line and then end and return the list to the variable in method 
        try { 
            Scanner fileInput = new Scanner(file) ; 
            
            //While there are entries, keep adding to array until no more 
            while (fileInput.hasNextLine()) { 
                stringText = fileInput.nextLine() ; 
                brokenString = stringText.split("::") ; 
                
                //Create member and add to the array 
                fullMemberList.add(new Member(brokenString[0], brokenString[1], 
                        brokenString[2], brokenString[3])) ; 
            }
        } catch (FileNotFoundException e) { 
            //Do nothing because it does not matter. Will just create it as needed 
        } 
        return fullMemberList ; 
    }
} 
/** 
 * Appendix 1: 
 * Output for server log - Not a proper "user 1, user 2 , etc. 
 * Because I destroy and recreate the active thread to be able to send more so have to stick with current solution
 * This means the server doesn't say "data from user x" it says "receiving data from current thread x" which is 
 * specific language 
 */