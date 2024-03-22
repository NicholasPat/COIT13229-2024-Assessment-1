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
public class TCPServer {    
    //Will become main method in the final 
    //public static void main(String args[]) { 
    public static void serverMain() { 
        int interval = 2000; //Print to file every 2 seconds
        java.util.Timer tm = new java.util.Timer(); 
        tm.schedule(new WriteObjectToFile(), interval, interval);
        
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
    
    //This makes the .txt file from the entries given which is to be made into an object via createFile() method 
    public static void updateTextFile(Member member) { 
        try { 
            FileWriter fw = new FileWriter("memberlist.txt", true) ; 
            PrintWriter pw = new PrintWriter(fw) ; 
            pw.write(member.getFirstName() + "--__--" + member.getLastName() + "--__--" + member.getAddress() + "--__--" + member.getPhoneNumber() + "\n") ; 
            pw.close() ; 
            fw.close() ; 
        } catch (IOException e) { 
            System.out.println("Members file doesn't exist") ; 
        }
    }
}

class Connection extends Thread { 
    ObjectInputStream in ; 
    ObjectOutputStream out ; 
    Socket clientSocket ; 
    int count = 1 ; 
    
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
            TCPServer.updateTextFile(member) ; 
            
        } catch(EOFException e){System.out.println("EOF:"+e.getMessage());
	} catch(IOException e) {System.out.println("readline:"+e.getMessage());
	} catch(ClassNotFoundException ex){
            ex.printStackTrace();
	}finally{ try {clientSocket.close();}catch (IOException e){/*close failed*/}}
    }
}

class WriteObjectToFile extends TimerTask { 
    @Override
    public void run() { 
        String fileName = "memberlistObject" ; 
        ArrayList<Member> completeMemberList = readTextFile() ;  
        FileOutputStream fos ; ObjectOutputStream out ; 
        try { 
            fos = new FileOutputStream(fileName) ;  
            out = new ObjectOutputStream(fos) ; 
            out.writeObject(completeMemberList) ; 
            out.close() ; 
            System.out.println("Member object persisted") ; 
            completeMemberList.clear() ; 
        } catch (IOException ex) { 
            ex.printStackTrace() ; 
        }
    }
    
    public static ArrayList<Member> readTextFile() { 
        File file = new File("memberlist.txt") ; 
        ArrayList<Member> fullMemberList = new ArrayList<>() ; 
        try { 
            Scanner fileInput = new Scanner(file) ; 
            int i = 0 ; 
            while (fileInput.hasNextLine()) { 
                String stringText = fileInput.nextLine() ; 
                String[] brokenString = stringText.split("--__--") ; 
                
                String firstName = brokenString[0] ; 
                String lastName = brokenString[1] ; 
                String address = brokenString[2] ; 
                String phoneNumber = brokenString[3] ; 
                
                //Create member and add to the array 
                fullMemberList.add(new Member(firstName, lastName, address, phoneNumber)) ; 
                //System.out.println(fullMemberList.get(i).toString()) ; 
                i++ ; 
            }
        } catch (FileNotFoundException e) { 
            System.out.println("Failed readTextFile") ;
        }
        return fullMemberList ; 
    }
}




/** 
 *  //To be read on startup and initialised into an array. Is this even needed here 
    //This is serialisation, so need to pick up the .txt file and convert to object 
    private static ArrayList<Member> readFile() { 
        String filename = "Members" ; 
        ArrayList<Member> members = null ; 
        FileInputStream fis ; 
        ObjectInputStream in ; 
        try { 
            fis = new FileInputStream(filename) ; 
            in = new ObjectInputStream(fis) ; 
            members = (ArrayList<Member>)in.readObject() ; 
            in.close() ; 
        }catch (IOException ex) {ex.printStackTrace();
        }catch (ClassNotFoundException ex) { ex.printStackTrace();} 
        if (members == null) { 
            return new ArrayList<>() ; 
        }
        return members ; 
    }
 */