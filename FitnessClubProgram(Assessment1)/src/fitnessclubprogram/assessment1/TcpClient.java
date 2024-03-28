package fitnessclubprogram.assessment1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Nicholas Paterno 12188564
 * TcpClient.java 
 * This class is what the customer would be interfacing with. As such all it is meant 
 * to do is to get the member details from the user, and then pass it (or them)
 * to the TcpServer which will add to .txt file 
 */
public class TcpClient {
    static int count = 0 ; 
    
    //Main class, get the members from the user and send the data to the server each time 
    public static void main(String args[]) { 
        Socket s = null ; 
        final int serverPort = 8864 ; 
        final String hostName = "localhost" ; 
        String message ; 
        String messageA ; 
        
        try { 
            
            //Creating a socket here as if it fails it will be throwing an error otherwise 
            //if not it queries count THEN throws error 
            s = new Socket(hostName, serverPort) ; 
            count = getMemberCount() ; 
            int i = 1 ; 
            
            while (count >0) { 
                //Creating the socket and Stream used 
                s = new Socket(hostName, serverPort) ; 
                DataInputStream in = new DataInputStream(s.getInputStream()) ; 
                DataOutputStream out = new DataOutputStream(s.getOutputStream()) ; 
                
                Member memberSend = getMemberDetails(i) ; 
                
                //Now this is a bit hacky, but the idea is silently adding the i count onto the String and sending it without it being known 
                //As such two strings are generated, one for the client and one for the server to work with 
                messageA = memberSend.getFirstName() + "::" + memberSend.getLastName() + "::" + 
                        memberSend.getAddress() + "::" + memberSend.getPhoneNumber() ; 
                message = messageA + "::" + i ; 
                
                System.out.println("Sending data to the server...........") ; 
                System.out.println(messageA) ; 
                
                //write to server, then receive response 
                out.writeUTF(message) ; 
                String data= in.readUTF() ; 
                
                System.out.println("Message received from the server: " + data) ; 
                System.out.println("____________________") ; 
                count-- ; i++ ; 
            }
        }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("readline:"+e.getMessage());
        }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
    
    //Ask user for count of members to add 
    private static int getMemberCount() { 
        int userCount = 0 ; 
        String number ; 
        
        while (userCount == 0) { 
            try { 
                number = queryUserCount() ; 
                userCount = Integer.parseInt(number) ; 
            } catch (NumberFormatException e) { 
                System.out.println("Error, please input an Integer value for the count") ; 
                userCount = 0 ; 
            }
        }
        return userCount ; 
    }
    
    //Simply asking the user for a count and then sending it back to the main method and dictates how many times to iterate and ask for member objects 
    //Don't know why I split this into this method instead of making a bulk method but it shows what this step is doing 
    private static String queryUserCount() { 
        String countA ; 
        Scanner input = new Scanner(System.in) ; 
        System.out.print("Please input number of users you wish to add: ") ; 
        countA = input.nextLine() ; //Error handle 
        return countA ; 
    }
    
    //Get the memeber details and create the object to send to be perpetuated by file. Ugly doing error checking with the "--__--" but not sure how else to handle it cleanly even though 
    //this is probably a way to do so 
    private static Member getMemberDetails(int localCount) { 
        Member tempMembers ; 
        String firstName = null ; 
        String lastName = null ; 
        String address = null ;
        String phoneNumber = null; 
        boolean check = false ; 
        
        System.out.println("Please input details for member: " + localCount) ; 
        
        while (!check) { 
            Scanner fNameInput = new Scanner(System.in) ; 
            System.out.print("Enter user's first name: ") ; 
            firstName = fNameInput.nextLine() ; 
            check = memberDetailsCheck(firstName, 1) ; 
        } check = false ; 
        
        while (!check) { 
            Scanner lNameInput = new Scanner(System.in) ; 
            System.out.print("Enter user's last name: ") ; 
            lastName = lNameInput.nextLine() ; 
            check = memberDetailsCheck(lastName, 1) ; 
        } check = false ; 
        
        while (!check) { 
            Scanner addressInput = new Scanner(System.in) ; 
            System.out.print("Enter user's address: ") ; 
            address = addressInput.nextLine() ; 
            check = memberDetailsCheck(address, 1) ; 
        } check = false ; 
        
        while (!check) { 
            Scanner phoneInput = new Scanner(System.in) ; 
            System.out.print("Enter user's phone number: ") ; 
            phoneNumber = phoneInput.nextLine() ; 
            check = memberDetailsCheck(phoneNumber, 2) ; 
        } 
        
        tempMembers = new Member(firstName, lastName, address, phoneNumber) ;
        return tempMembers ; 
    }
    
    //Simply, because the text of the text file is split in a specific way, error handle using that identifying string. It is incredibly unlikely to be written, but just making sure 
    //as if I don't handle this it will break the program when the server attempts to read it 
    private static boolean memberDetailsCheck(String text, int tag) { 
        boolean result = text.contains("::") ; 
        if (result) { 
            System.out.println("Invalid String text '::' detected. "
                    + "Please input again without this") ; 
            return false ; 
        }
        
        //Specifically testing the phone number for two checks 
        if (tag == 2) { 
            //Testing if 10 digits long 
            String regexStr = "^[0-9]{10}$" ; 
            if (!text.matches(regexStr)) { 
                System.out.println("Number must be in format 10 digits long") ; 
                return false ; 
            } 
        }
        return true ; 
    }
}

//Code discard 
//Create the member object to send to the server then send 
//Member memberToSend memberToSend = getMemberDetails() ; 
//out.writeObject(memberToSend) ; 

//Output and count down until 0 then stop 
//System.out.println("Sending data to the server...........") ; 
//System.out.println("Sent to server") ; 
//System.out.println("____________________") ; 

//ObjectOutputStream out ; 
//out = new ObjectOutputStream(s.getOutputStream()) ; 