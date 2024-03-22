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
import java.util.Arrays;

/**
 * @author linke
 */
public class UDPServer {
    
    //Most of the code borrowed from the tute, but adapted to fit the member details sending requirement 
    public static void run(){ 
    	DatagramSocket aSocket = null;
        ArrayList<Member> completeList = writeToList() ; 
        if (completeList.isEmpty()) { 
            System.out.println("No entries in memberobjectList, voiding the attempt for reading the file, please try again later") ; 
            return ; 
        }
        
        try{
            aSocket = new DatagramSocket(2264);
            
            byte[] buffer = new byte[1000];
            while(true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                byte[] currentObject ; 
                int length ; 
                
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
    private static byte[] serialiseObject(Member member) throws IOException{ 
        ByteArrayOutputStream out = new ByteArrayOutputStream() ; 
        ObjectOutputStream os = new ObjectOutputStream(out) ; 
        os.writeObject(member) ; 
        os.close() ; 
        return out.toByteArray() ; 
    } 
    
    //To be removed but used in the meantime to check if the ArrayList<> is being populated correctly 
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
