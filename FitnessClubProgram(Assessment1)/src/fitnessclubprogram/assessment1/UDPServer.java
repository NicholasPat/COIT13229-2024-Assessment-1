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
 * @author linke
 */
public class UDPServer {
    
    //Most of the code borrowed from the tute, but adapted to fit the member details sending requirement 
    public static void run(){ 
        //Creating the socket and creating an ArrayList<> of the deserialised member objects from file 
    	DatagramSocket aSocket = null;
        ArrayList<Member> completeList = writeToList() ; 
        if (completeList.isEmpty()) { 
            System.out.println("No entries in memberobjectList, voiding the attempt for reading the file, please try again later") ; 
            return ; 
        }
        
        //Send and receive from the server while it is active 
        try{
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
                    DatagramPacket reply = new DatagramPacket(currentObject, length, request.getAddress(), request.getPort()) ; 
                    aSocket.send(reply);
                }
            }
        }catch (SocketException e){System.out.println("Socket: " + e.getMessage());
        }catch (IOException e) {System.out.println("IO: " + e.getMessage());
        }finally {if(aSocket != null) aSocket.close();}
    }
    
    //Get the memberlistObject file and deserialise it to ArrayList<> and then send to the main method 
    private static ArrayList<Member> writeToList() { 
        ArrayList<Member> memberList = new ArrayList<>() ; 
        String fileName = "memberlistObject" ; 
        FileInputStream fis ; 
        ObjectInputStream in ; 
        
        try { 
            fis = new FileInputStream(fileName) ; 
            in = new ObjectInputStream(fis) ; 
            memberList = (ArrayList<Member>)in.readObject() ; 
            in.close() ; 
        } catch (IOException ex) {ex.printStackTrace();
        } catch (ClassNotFoundException ex) {ex.printStackTrace() ;}
        
        if (memberList.isEmpty()) { 
            return new ArrayList<>() ; 
        } 
        
        //DEBUG - REMOVE LATER 
        debugPrint(memberList) ; 
        
        return memberList ; 
    }
    
    //Source for this: https://stackoverflow.com/questions/3736058/java-object-to-byte-and-byte-to-object-converter-for-tokyo-cabinet
    //As far as I can explain, creates a byte array which is then initialised with the object output which the then writes the member object to be sent back to the main method as a BYTE[]
    private static byte[] serialiseObject(Member member) throws IOException{ 
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

/**
 * byte[] fillerString = "--__--".getBytes() ; 
                String objectString = completeList.get(0).getFirstName() + fillerString + completeList.get(0).getLastName() + fillerString + completeList.get(0).getAddress() +
                        fillerString + completeList.get(0).getPhoneNumber() ; 
                
                byte[] objectCurrent = objectString.getBytes() ; 
 */

//INSTEAD OF TO STRING just use get name, get address, etc and use the --__-- splitter and then send the string, then unpack it on the other end and then display results 
//Potential issue, I dunno if I can send that many requests concurrently. Play around. But toString ain't gonna work 

//Arrays.toString(objectCurrent).length()
//DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(), request.getPort());

//completeList.get(i).getFirstName() + fillerString + completeList.get(i).getLastName() + fillerString + completeList.get(i).getAddress() + fillerString + completeList.get(i).getPhoneNumber() ; 
//System.out.println("Client Request: " + new String(request.getData(), 0, request.getLength()) + " at port: " + request.getPort() + " at address: " + request.getAddress() + "");
//System.out.println("Current byte: " + currentObject.toString() + " \nCurrent length: " + length) ; 
