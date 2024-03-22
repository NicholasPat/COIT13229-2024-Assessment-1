package fitnessclubprogram.assessment1;

import java.util.Scanner;

/**
 * @author linke
 */

public class FitnessClubProgramAssessment1 {    
    public static void main(String[] args) throws ClassNotFoundException {
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
            case "1" -> TCPClient.socketConnection() ;
            case "2" -> TCPServer.serverMain() ;
            case "3" -> {UDPClient.run(); ;}
            case "4" -> UDPServer.run() ;
            default -> System.out.println("How did you break this?") ;
        }
        
    }
}
