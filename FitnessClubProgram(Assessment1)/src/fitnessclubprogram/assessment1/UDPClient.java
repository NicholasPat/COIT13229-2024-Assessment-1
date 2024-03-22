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
   private static String objectRequest = "memberListObject";
   private static String hostName;
   
   public static void run() throws ClassNotFoundException { 
	DatagramSocket aSocket = null;
        hostName = "localhost";  
        try {
            //Assignments for pinging the server
            aSocket = new DatagramSocket() ; 
            byte [] m = objectRequest.getBytes() ;
            InetAddress aHost = InetAddress.getByName( hostName) ;
            int serverPort = 2264 ; 
            DatagramPacket request = new DatagramPacket(m,  objectRequest.length(), aHost, serverPort) ;
            
            //Assignments for the responses 
            Object intermediateMember ; 
            Member member ; 
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply ; 
            int count = 0 ; 
            
            while (true) { 
                reply = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(reply);
                intermediateMember = deserialiseObject(reply.getData()) ; 
                member = Member.class.cast(intermediateMember) ; 
                
                //Actual output -- Omg this is so hacky I dunno how else to do it. But it basically is saying: Do this once and only once 
                if (count == 0) { 
                    System.out.println("Response from server:") ; 
                    System.out.format("%18s%16s%28s%14s", "First Name|", "Last Name|", "Address|", "Phone Number" + "\n") ; 
                }
                
                System.out.format("%18s%16s%28s%14s%n", member.getFirstName(), member.getLastName(), member.getAddress(), member.getPhoneNumber()) ; 
                count++ ; 
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e){System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
   }
   
   //From https://stackoverflow.com/questions/3736058/java-object-to-byte-and-byte-to-object-converter-for-tokyo-cabinet
   public static Object deserialiseObject(byte[] data) throws IOException, ClassNotFoundException { 
       ByteArrayInputStream in = new ByteArrayInputStream(data) ; 
       ObjectInputStream is = new ObjectInputStream(in) ; 
       Object member = is.readObject() ;
       return member ; 
   }
}

/** 
 * //Display the reply -- Not needed 
    String response=new String(reply.getData(), 0, reply.getLength());
    String port = Integer.toString(reply.getPort()) ; 
    System.out.print("Server Response: Received response: "+ response + " \nFrom server at port: " + port + " \nServer at address: " + reply.getAddress() + "\n") ;
    replyString = new String(reply.getData(), 0, reply.getLength()) ; 
    System.out.println("This is the converted to string reply data: " + replyString) ; 
    System.out.println("This is the raw reply data" + reply.getData()) ; 
 */