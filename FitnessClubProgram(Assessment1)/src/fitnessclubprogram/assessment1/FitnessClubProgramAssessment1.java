package fitnessclubprogram.assessment1;

import java.util.Scanner;

/**
 * @author linke
 */
public class FitnessClubProgramAssessment1 {
    static TCPClient tcpClient = new TCPClient() ; 
    static TCPServer tcpServer = new TCPServer() ; 
    static UDPClient udpClient = new UDPClient() ; 
    static UDPServer udpServer = new UDPServer() ; 
    //Member members = new Member() ; 
    
    public static void main(String[] args) {
        //What I am doing here, for the beginning at least is for testing choose the testing parameters via this one program 
        //The actual program will handle this differently but making it hacky atm. I think it has to be different .jar -- 
        //files 
        
        String instanceForTest = "0" ; 
        boolean state = true ; 
        //0 = Nothing, 1 = TCP Client, 2 = TCP Server, 3 = UDP Client, 4 = UDP Server 
        while (state == true) { 
            Scanner input = new Scanner(System.in) ; 
            System.out.print("Please input the testing parameter desired, input = INT (1,4): " ) ; 
            instanceForTest = input.nextLine() ; 
            System.out.println() ; 
            if (instanceForTest.equals("1") || (instanceForTest.equals("2")) || (instanceForTest.equals("3")) || (instanceForTest.equals("4")) ){ 
                state = false ; 
            }
        }
        
        switch (instanceForTest) {
            case "1":
                //TCP Client 
                //Customer front end that gathers the information for member(s) and send it to the TCP server for adding to persistance 
                
                tcpClient.socketConnection() ; 
                break;
            case "2":
                //TCP Server 
                //Receives request from TCP Client(s) and adds their entries to the persisting file 
                tcpServer.serverMain() ; 
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
}
