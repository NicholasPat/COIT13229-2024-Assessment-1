package fitnessclubprogram.assessment1;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author linke
 */
public class TCPClient {
    
    public static Member generateMembers() { 
        //This will be where all the functionality for the TCP Client will take place 
        
        /** 
         * Pseudocode! 
         * Ask for number of members the user wishes to add 
         * Ask for member details and create the member object 
         * Send the data to the "server" and then output customer data 
         * Send out server response "save data for member number 1" 
         *      For debug, add port + IP address 
         */
        
        //Getting count of users. Error handling so text will result in a repeat of the count 
        int userCount = 0 ; String number ; 
        while (userCount == 0) { 
            try { 
                number = queryUserCount() ; 
                userCount = Integer.parseInt(number) ; 
                System.out.println("Given count: " + userCount) ; 
            } catch (NumberFormatException e) { 
                System.out.println("Error, please input an Integer value for the count") ; 
                userCount = 0 ; 
            }
        }
        
        //Getting member details. Using the count can define a temp array to hold the members which will then be pushed...
        //To the server and perpetuated into the file 
        Member[] memberListArray ; Member tempMember = null ; 
        
        while (userCount >0) { 
            tempMember = getMemberDetails() ; 
            
        }
        return null ; 
    }
    
    //Will become main? 
    //public static void main(String args[]) { 
    public static void socketConnection() { 
        Socket s = null ; 
        try { 
            int serverPort = 8864 ; 
            
            s = new Socket("localhost", serverPort) ; 
            
            ObjectInputStream in = null ; 
            ObjectOutputStream out = null ; 
            
            out = new ObjectOutputStream(s.getOutputStream()) ; 
            in = new ObjectInputStream(s.getInputStream()) ; 
            
            //At this point call the methods for getting the user to input the Members 
            
            Member memberToSend ; 
            int count = getMemberCount() ; 
            while (count >0) { 
                memberToSend = getMemberDetails() ; 
                out.writeObject(memberToSend) ; 
                System.out.println("Sending data to the server...........") ; 
                System.out.println(memberToSend.getFirstName() + ":" + memberToSend.getLastName() + ":" + memberToSend.getAddress() + ":" + memberToSend.getPhoneNumber()) ; 
                System.out.println("____________________") ; 
                count-- ; 
            }
            
        }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
        }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
        }catch (IOException e){System.out.println("readline:"+e.getMessage());
        }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
    }
    
    private static int getMemberCount() { 
        int userCount = 0 ; String number ; 
        while (userCount == 0) { 
            try { 
                number = queryUserCount() ; 
                userCount = Integer.parseInt(number) ; 
                System.out.println("Given count: " + userCount) ; 
            } catch (NumberFormatException e) { 
                System.out.println("Error, please input an Integer value for the count") ; 
                userCount = 0 ; 
            }
        }
        
        return userCount ; 
    }
    
    private static String queryUserCount() { 
        String count ; 
        Scanner input = new Scanner(System.in) ; 
        System.out.print("Please input number of users you wish to add: ") ; 
        count = input.nextLine() ; 
        
        return count ; 
    }
    
    private static Member getMemberDetails() { 
        Member tempMembers ; 
        String firstName, lastName, address, phoneNumber ; 
        
        Scanner fNameInput = new Scanner(System.in) ; 
        System.out.print("Enter user's first name: ") ; 
        firstName = fNameInput.nextLine() ; 
        
        //System.out.println() ; 
        
        Scanner lNameInput = new Scanner(System.in) ; 
        System.out.print("Enter user's last name: ") ; 
        lastName = lNameInput.nextLine() ; 
        
        //System.out.println() ; 
        
        Scanner addressInput = new Scanner(System.in) ; 
        System.out.print("Enter user's address: ") ; 
        address = addressInput.nextLine() ; 
        
        //System.out.println() ; 
        
        Scanner phoneInput = new Scanner(System.in) ; 
        System.out.print("Enter user's phone number: ") ; 
        phoneNumber = phoneInput.nextLine() ; 
        
        //System.out.println() ; 
        
        tempMembers = new Member(firstName, lastName, address, phoneNumber) ; 
        
        System.out.println("Details of member object: " + tempMembers.toString()) ; 
        return tempMembers ; 
    }
    
}
