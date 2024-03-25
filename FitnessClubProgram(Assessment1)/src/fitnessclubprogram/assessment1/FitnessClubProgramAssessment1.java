package fitnessclubprogram.assessment1;

import java.util.Scanner;

/**
 * @author linke
 */

public class FitnessClubProgramAssessment1 {    
    public static void main(String[] args) throws ClassNotFoundException {
        String instanceForTest = "0" ; 
        boolean state = true ; 
        //0 = Nothing, 1 = TCP Client, 2 = TCP Server, 3 = UDP Client, 4 = UDP Server 
        while (state == true) { 
            Scanner input = new Scanner(System.in) ; 
            System.out.print("Please input the testing parameter desired, input = INT (1,4): " ) ; 
            instanceForTest = input.nextLine() ; 
            System.out.println() ; 
            if (instanceForTest.matches("1|2|3|4")){ 
                state = false ; 
            }
        }
        
        switch (instanceForTest) {
            case "1" -> TcpClient.run() ;
            case "2" -> TcpServer.run() ;
            case "3" -> {UdpClient.run(); }
            case "4" -> UdpServer.run() ;
            default -> System.out.println("How did you break this?") ;
        }
    }
}