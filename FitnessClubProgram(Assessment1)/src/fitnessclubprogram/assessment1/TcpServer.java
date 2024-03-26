package fitnessclubprogram.assessment1;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimerTask;

/**
 * @author linke
 */
public class TcpServer {    
    //Will become main method in the final 
    //public static void main(String args[]) { 
    public static void run() { 
        //Code provided by Uni 
        int interval = 2000; //Print to file every 2 seconds
        java.util.Timer tm = new java.util.Timer(); 
        tm.schedule(new WriteObjectToFile(), interval, interval);
        
        //Try to create the server connection and keep it open 
        try { 
            int serverPort = 8864 ; 
            ServerSocket listenSocket = new ServerSocket(serverPort) ; 
            
            while (true) { 
                Socket clientSocket = listenSocket.accept() ; 
                Connection c = new Connection(clientSocket) ; //Doesn't matter if reads as unused as it does what it needs to do 
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
    int count = 1 ; 
    
    //Constructor for each connection made to the server, and it can do multiple and works as intended 
    public Connection(Socket aClientSocket) { 
        try { 
            clientSocket = aClientSocket ; 
            in = new ObjectInputStream(clientSocket.getInputStream()) ; 
            out = new ObjectOutputStream(clientSocket.getOutputStream()) ; 
            this.start() ; //Initiate the thread 
        } catch (IOException e) { System.out.println("Conncetion: " + e.getMessage()) ; }
    }
    
    //The actual input done by the client, so takes member object given, calls the write to file method, then writes to file 
    public void run() { 
        try { 
            Member member = (Member)in.readObject() ; 
            System.out.println("Receiving data from client: " + (count++)) ; 
            System.out.println(member.toString()) ;
            //Server output to client 
            updateTextFile(member) ; 
            
        } catch(EOFException e){System.out.println("EOF:"+e.getMessage());
        } catch(IOException e) {System.out.println("readline:"+e.getMessage());
        } catch(ClassNotFoundException ex){ //ex.printStackTrace();
        }finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
    }
    
    //This makes the .txt file from the entries given which is to be made into an object via createFile() method 
    public static void updateTextFile(Member member) { 
        //Try and check for file, and if there just append to the end and don't recreate the whole thing by iterating over an array and adding them all 
        //The identifying string is put in as it makes it easier later to cut the string using that identifier, I feel ":" wouldn't have been good as could be used in some contexts 
        try { 
            //Printwriter: Writes to the nested FileWriter which writes to the file 
            FileWriter fw = new FileWriter("memberlist.txt", true) ; //"true" tag makes it appendable 
            PrintWriter pw = new PrintWriter(fw) ; 
            pw.write(member.getFirstName() + "--__--" + member.getLastName() + "--__--" + member.getAddress() + 
                    "--__--" + member.getPhoneNumber() + "\n") ; 
            
            //Make sure to close or else it keeps trying and bloats the file. Made that mistake before 
            pw.close() ; 
            fw.close() ; 
        } catch (IOException e) { System.out.println("Members file doesn't exist") ; }
    }
}

class WriteObjectToFile extends TimerTask { 
    
    //Each time the timer ticks 2 seconds, do this, which is just make an array by reading the file in the system and then serialising the objects into an object file 
    //If the program became much larger and had more entries to cause lag in doing this step, I imagine there'd be ways to do like, caching or saying go down up and append 
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
            out.writeObject(completeMemberList) ; 
            out.close() ; 
            System.out.println("Member object persisted") ; 
            completeMemberList.clear() ; 
        } catch (IOException ex) { ex.printStackTrace() ; }
    }
    
    //Readng the txt file and then making the memberobjectList file. Basically read the string line by line, chop it up into an array and remove the "--__--" identifier 
    private static ArrayList<Member> readTextFile() { 
        //Definitions 
        File file = new File("memberlist.txt") ; 
        ArrayList<Member> fullMemberList = new ArrayList<>() ; 
        
        //Attempt to read the file, if successful read into the array line by line and then end and return the list to the variable in method 
        try { 
            Scanner fileInput = new Scanner(file) ; 
            int i = 0 ; 
            while (fileInput.hasNextLine()) { 
                String stringText = fileInput.nextLine() ; 
                String[] brokenString = stringText.split("--__--") ; 
                
                //Assign to variables, although could create the object and then do .set___() 
                String firstName = brokenString[0] ; 
                String lastName = brokenString[1] ; 
                String address = brokenString[2] ; 
                String phoneNumber = brokenString[3] ; 
                
                //Create member and add to the array 
                fullMemberList.add(new Member(firstName, lastName, address, phoneNumber)) ; 
                i++ ; 
            }
        //Honestly, I won't error handle this more, since the program still functions even if there is no file present, it creates it immeidately as needed then keeps going 
        } catch (FileNotFoundException e) { System.out.println("Failed readTextFile") ; } 
        
        return fullMemberList ; 
    }
} 