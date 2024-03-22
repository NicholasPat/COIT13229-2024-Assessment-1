package fitnessclubprogram.assessment1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author linke
 */

public class UDPClient {
    //Assignments 
    private static String objectRequest = "memberListObject";
    private static String hostName;
   
    public static void run() throws ClassNotFoundException { 
        //Assignments 
        DatagramSocket aSocket = null;
        hostName = "localhost";  
        
        try {
            //Assignments for pinging the server
            aSocket = new DatagramSocket() ; 
            byte [] m = objectRequest.getBytes() ;
            InetAddress aHost = InetAddress.getByName( hostName) ;
            int serverPort = 2264 ; 
            DatagramPacket request = new DatagramPacket(m,  objectRequest.length(), 
                    aHost, serverPort) ;
            
            aSocket.send(request); //Send the reauest prepared 
            
            //Assignments for the responses 
            Object intermediateMember ; 
            Member member ; 
            byte[] buffer = new byte[1000];
            DatagramPacket reply ; 
            int count = 0 ; 
            
            //While the connection is active, do this. Connection should always be active so will be forever open. Not an issue though as it works. Could add a timer in some way to stop 
            //after nothing happening for x time 
            while (true) { 
                //Assignments 
                reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                intermediateMember = deserialiseObject(reply.getData()) ; //General object to be cast to Member 
                member = Member.class.cast(intermediateMember) ; //Cast back to Member 
                
                //Actual output -- It basically is saying: Do this once and only once 
                if (count == 0) { 
                    System.out.println("Response from server:") ; 
                    System.out.format("%18s%16s%28s%14s", "First Name|", "Last Name|", "Address|", "Phone Number" + "\n") ; 
                }
                
                System.out.format("%18s%16s%28s%14s%n", member.getFirstName(), member.getLastName(), 
                        member.getAddress(), member.getPhoneNumber()) ; 
                count++ ; 
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
   }
   
   //From https://stackoverflow.com/questions/3736058/java-object-to-byte-and-byte-to-object-converter-for-tokyo-cabinet
   //The Member object for some reason to deserialised to a general object which will then be cast into its Member class 
   public static Object deserialiseObject(byte[] data) throws IOException, ClassNotFoundException { 
       ByteArrayInputStream in = new ByteArrayInputStream(data) ; 
       ObjectInputStream is = new ObjectInputStream(in) ; 
       Object member = is.readObject() ;
       return member ; 
   }
}