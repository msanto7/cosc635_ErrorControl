/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sawmarisa;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author Ritzarolli
 * @version 12012019.1440
 */
public class SAWserver {
    
    public static DatagramSocket socket;
    
    public static void main(String[] args) throws SocketException {
        
        socket = new DatagramSocket(6700);
        
        
        //ask user for number representing percentage of simulated packet loss
        Scanner s = new Scanner(System.in);
        System.out.println("To simulate a percentage of packet loss, please enter a number 0-99: ");
        int userNum = s.nextInt();
    
        //checks that number input is in valid range
        if ((userNum <0) || (userNum >99)){
            System.out.println("Please enter a number between 0-99: ");
            userNum = s.nextInt();
    }

    
    }
    
    /**
     * Method simulates a packet drop by using a random number generator.
     * The user inputs a number 1-99
     * If the random number matches the user's number, the packet is dropped.
     * This method is run before each packet transmission.
     * @return 
     */
    private static String dropSimulator(int userNum){
        //random number generator with current system time as the seed
        Random chance = new Random(System.currentTimeMillis());
        //binds the random value to between 0 and 99
        int rand = chance.nextInt(100);
        
        if (userNum == rand){
         return "Packet dropped.\n"; //TODO make it plug in packet sequence number?   
        }
        else{
        return "";
        }
    }
}


/**FROM PROJECT SPECIFICATION
 * 
 * To simulate the packet loss we will use pseudo random number to control that. 
 * 
 * a.First your program will ask the user to input a number between [0, 99]. 
 * This number means the percentage of packet losses will happen in the transmission.  
 * 
 * b.Then each time before you send your protocol data unit, your program will generate 
 * a pseudo random number in the range [0, 99] with the random seed set to the current system time. 
 * If the random number generated is less than the user input number then the 
 * current protocol data unit won't be sent to simulate a packet loss.
 */