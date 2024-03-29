package fitnessclubprogram.assessment1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Nicholas Paterno 12188564
 * UdpClient.java 
 * This is used to communicate with the UdpServer and get the member list one at a time and then output that list 
 * Formatted to be in tabular format to be more human friendly 
 */
public class UdpClient {
    //Assignments 
    private static String objectRequest = "memberListObject";
    private static String hostName;
   
    public static void main(String args[]) throws ClassNotFoundException { 
        //Assignments 
        DatagramSocket aSocket = null;
        hostName = "localhost";  
        
        //Try and send the server a request, then receive members as reply. Output to console
        try {
            //Assignments for pinging the server
            aSocket = new DatagramSocket() ; 
            byte [] m = objectRequest.getBytes() ;
            InetAddress aHost = InetAddress.getByName( hostName) ;
            int serverPort = 2264 ; 
            DatagramPacket request = new DatagramPacket(m,  objectRequest.length(), 
                    aHost, serverPort) ;
            
            aSocket.send(request); //Send the prepared request 
            
            //Assignments for the responses 
            Object intermediateMember ; 
            Member member ; 
            byte[] buffer = new byte[1000] ;
            DatagramPacket reply ; 
            int count = 0 ; 
            
            //Appendix 1
            while (true) { 
                //Assignments 
                reply = new DatagramPacket(buffer, buffer.length) ;
                aSocket.receive(reply) ; 
                
                //Intermediate member is of the Object class, so it then needs to be cast back into a Memnber to then be printed 
                intermediateMember = deserialiseObject(reply.getData()) ; //General object to be cast to Member 
                member = Member.class.cast(intermediateMember) ; //Cast back to Member 
                
                //Actual output -- It basically is saying: Do this once and only once 
                if (count == 0) { 
                    System.out.println("Response from server:") ; 
                    System.out.format("%18s%16s%28s%14s", "First Name|", 
                            "Last Name|", "Address|", "Phone Number" + "\n") ; 
                }
                
                System.out.format("%18s%16s%28s%14s%n", member.getFirstName(), member.getLastName(), 
                        member.getAddress(), member.getPhoneNumber()) ; 
                count++ ; 
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
   }
   
   //The Member object for some reason to deserialised to a general object which will then be cast into its Member class 
   public static Object deserialiseObject(byte[] data) throws IOException, ClassNotFoundException { 
       //Byte Array is what is what the "data" variable is. So what this is saying is create a byte array stream then take that array and turn it into an Object 
       ByteArrayInputStream in = new ByteArrayInputStream(data) ; 
       ObjectInputStream is = new ObjectInputStream(in) ; 
       
       //Take that object stream and read the object sent and cast it to the Object class, which is a general Class to be converted back in the main method 
       Object member = is.readObject() ;
       return member ; 
   }
}

/** 
 * Appendix 1: 
 * While the connection is active, do this. Connection should always be active so will be forever open. Not an issue though as it works 
 * Could add a timer in some way to stop after nothing happening for x time but not sure. Or could get the total number of the array and just make it work 
 * until that point. But for now there is no need 
 */