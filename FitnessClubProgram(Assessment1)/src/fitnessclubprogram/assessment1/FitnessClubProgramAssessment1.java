package fitnessclubprogram.assessment1;

import java.util.Scanner;

/**
 * @author linke
 */
public class FitnessClubProgramAssessment1 {
    TCPClient tcpClient = new TCPClient() ; 
    TCPServer tcpServer = new TCPServer() ; 
    UDPClient udpClient = new UDPClient() ; 
    UDPServer udpServer = new UDPServer() ; 
    Member members = new Member() ; 
    
    public static void main(String[] args) {
        //What I am doing here, for the beginning at least is for testing choose the testing parameters via this one program 
        //The actual program will handle this differently but making it hacky atm 
        
        String instanceForTest = "0" ; 
        boolean state = true ; 
        //0 = Nothing, 1 = TCP Client, 2 = TCP Server, 3 = UDP Client, 4 = UDP Server 
        while (state == true) { 
            Scanner input = new Scanner(System.in) ; 
            System.out.println("Please input the testing parameter desired, input = INT (1,4)" ) ; 
            instanceForTest = input.nextLine() ; 
            if (instanceForTest.equals("1") || (instanceForTest.equals("2")) || (instanceForTest.equals("3")) || (instanceForTest.equals("4")) ){ 
                state = false ; 
            }
        }
        
        switch (instanceForTest) {
            case "1":
                //TCP Client 
                //Customer front end that gathers the information for member(s) and send it to the TCP server for adding to persistance 
                
                break;
            case "2":
                //TCP Server 
                //Receives request from TCP Client(s) and adds their entries to the persisting file 
                
                break;
            case "3":
                //UDP Client 
                //Manager front end to acces records of member object 
                
                break;
            case "4":
                //UDP Server 
                //Receive manager request and send back member object to be displayed through UDP Client class 
                
                break;
            default:
                //Ignore, not gonna be invoked as not necessary 
                break;
        }
        
    }
    
    private Member getMemberDetails() { 
        Member tempMembers ; 
        //First name, Last name, address, phone number. All Strings 
        String firstName = null ; String lastName = null ; String address = null ; String phoneNumber = null ; 
        
        Scanner fNameInput = new Scanner(System.in) ; 
        System.out.println("Enter users first name: ") ; 
        firstName = fNameInput.nextLine() ; 
        
        System.out.println() ; 
        
        Scanner lNameInput = new Scanner(System.in) ; 
        System.out.println("") ; 
        lastName = lNameInput.nextLine() ; 
        
        System.out.println() ; 
        
        Scanner addressInput = new Scanner(System.in) ; 
        System.out.println("") ; 
        address = addressInput.nextLine() ; 
        
        System.out.println() ; 
        
        Scanner phoneInput = new Scanner(System.in) ; 
        System.out.println() ; 
        phoneNumber = phoneInput.nextLine() ; 
        
        tempMembers = new Member(firstName, lastName, address, phoneNumber) ; 
        return tempMembers ; 
    }
    
}
