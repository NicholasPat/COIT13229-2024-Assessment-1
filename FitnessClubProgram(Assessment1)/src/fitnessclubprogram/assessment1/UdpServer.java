package fitnessclubprogram.assessment1;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * @author linke 12188564
 * UdpServer.java 
 * This is class is back-end of the UDP side of things. This takes the memberobjectList file and converts it to 
 * the member array and sends it back to the UdpClient to be displayed in tabular form 
 */
public class UdpServer {
    
    //Most of the code borrowed from the tute, but adapted to fit the member details sending requirement 
    public static void main(String args[]) { 
        //Creating the socket and creating an ArrayList<> of the deserialised member objects from file 
    	DatagramSocket aSocket = null;
        ArrayList<Member> completeList = writeToList() ; 
        
        //If the entry is empty then display this error and exit the method 
        if (completeList.isEmpty()) { 
            System.out.println("No entries in memberobjectList, voiding the attempt for reading the file, please try again later") ; 
            return ; 
        }
        
        //Send and receive from the server while it is active 
        try{
            //Definitions 
            aSocket = new DatagramSocket(2264) ;
            byte[] buffer = new byte[1000] ;
            
            //While the connection is active, do this chunk. Will forever iterate but frankly it doesn't need to change as it works, I imagine the fix might be to send a message from the 
            //client the server can then read as an END message. Probs a string or null object and if it errors just catch it and have the block say "End of stream" or something 
            while(true){
                //Create the packet to prepare for sending. Catch the request and get the port, and address from it as can't hard code these in, have to be dynamic 
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                byte[] currentObject ; 
                int length ; 
                
                //This is iterating over the array and sending the data one packet at a time 
                for (int i = 0; i < completeList.size(); i++) { 
                    currentObject = serialiseObject(completeList.get(i)) ; 
                    length = currentObject.length ; 
                    DatagramPacket reply = new DatagramPacket(currentObject, length, request.getAddress(), 
                            request.getPort()) ; 
                    aSocket.send(reply);
                }
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
    
    //Get the memberlistObject file and deserialise it to ArrayList<> and then send to the main method 
    //Suppressing unchecked warning as it does work, dunno why it happens though 
    @SuppressWarnings("unchecked")
    private static ArrayList<Member> writeToList() { 
        //Definitions 
        ArrayList<Member> memberList = new ArrayList<>() ; 
        String fileName = "memberlistObject" ; 
        FileInputStream fis ; 
        ObjectInputStream in ; 
        
        //Try to read in the file and deserialise 
        try { 
            fis = new FileInputStream(fileName) ; 
            in = new ObjectInputStream(fis) ; 
            memberList = (ArrayList<Member>)in.readObject() ; 
            in.close() ; 
        } catch (IOException ex) {ex.printStackTrace();
        } catch (ClassNotFoundException ex) {ex.printStackTrace() ;}
        
        //If empty then return an empty list. Null will make a huge issue if returned, dealt with that issue in a previous unit 
        if (memberList.isEmpty()) { 
            return new ArrayList<Member>() ; 
        } 
        
        debugPrint(memberList) ; //Not actually debug anymore. Choosing to leave as it is good for the server to output and show it's initialised the array as necessary
        return memberList ; 
    }
    
    //As far as I can explain, creates a byte array which is then initialised with the object output which the then writes the member object to be sent back to the main method as a BYTE[]
    private static byte[] serialiseObject(Member member) throws IOException{ 
        //Create a new byte array stream and cast to object output stream, then write in the member object and return the byte array 
        ByteArrayOutputStream out = new ByteArrayOutputStream() ; 
        ObjectOutputStream os = new ObjectOutputStream(out) ; 
        os.writeObject(member) ; 
        os.close() ; 
        return out.toByteArray() ; 
    } 
    
    //Just an extra bit of output for the server to log, it just shows the user that the Array is populating correctly 
    private static void debugPrint(ArrayList<Member> memberList) { 
        for (int i = 0; i < memberList.size(); i++) { 
            System.out.println("Current object: " + i + " " + memberList.get(i).toString()) ; 
        }
        System.out.println("End of entry list") ; 
    }
}