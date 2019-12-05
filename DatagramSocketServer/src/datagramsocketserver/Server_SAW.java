
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney

package datagramsocketserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


// SERVER -- Sender 
public class Server_SAW {       
    
    public static void main(String[] args)
    {
        int portNumServer = 50000;        
        int portNumClient = 50001;
        int numPacketsSent = 0;
        int numPacketsLost = 0;
        long startTime = 0;
        long endTime = 0;
        
        // Scanner obj for user input
        Scanner input = new Scanner(System.in);
        
        // prompt user for random number to simulate packet loss         
        int userInput;        
        do {
            System.out.println(" Please enter a number between [0,99]. ");
            while (!input.hasNextInt()) {
                System.out.println("Input is not a number, please try again.");
                input.next();
            }
            userInput = input.nextInt();
        } while (userInput < 0 || userInput > 99);
        System.out.println("Number " + userInput + " recieved. ");                           
        
        // set up socket for client
        try (DatagramSocket serverSocket = new DatagramSocket(portNumServer))
        {
            // read the input file into an array of bytes 
            File inputFile = new File("src/datagramsocketserver/COSC635_P2_DataSent.txt");        
            byte[] inputBytes = fileToBytes(inputFile);
            
            // attempt to send data while input file still has text
            for (int i = 0; i < inputBytes.length; i = i + 1024) {
                // save the start time 
                startTime = System.nanoTime();
                
                // initialize acknowledgement bool to false
                boolean ack = false;
                
                // create new byte array of the section we want for this specific packet
                byte[] packetBytes = Arrays.copyOfRange(inputBytes, i, (i + 1024));
                
                // create packet to send to the client 
                DatagramPacket datagramPacket = new DatagramPacket(
                        packetBytes,
                        packetBytes.length,
                        InetAddress.getLocalHost(),
                        portNumClient
                );

                while (ack == false) {
                    
                    if (DropPacket(userInput)) {
                        //packet is dropped - update counter
                        numPacketsLost++;
                    } else {
                        // send the udp packet to the client 
                        serverSocket.send(datagramPacket);                        
                    }
                    
                    // resend data if timeout is reached befire ack is recieved 
                    serverSocket.setSoTimeout(3000);
                    
                    try {
                        // recieve ack
                        byte[] buffer = new byte[1024];
                        DatagramPacket ackPacket = new DatagramPacket(buffer, buffer.length);
                        serverSocket.receive(ackPacket);
                        String ackMessage = "received packet";
                        numPacketsSent++;
                        ack = true;                    
                    }
                    catch (SocketTimeoutException e) {
                        System.out.println("Acknowledgment not recieved after 3 seconds, resending data");
                    }                                                                                    
                }                                
            } 
            
            // save stop time when sending data has finished 
            endTime = System.nanoTime();
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        //Print out Basic Statistics
        System.out.println("The server took " + (endTime - startTime) + " nanoseconds to complete sending all of the data.");
        System.out.println("The server successfully sent " + numPacketsSent + " packets. ");
        System.out.println("The server dropped " + numPacketsLost + " packets (simulated). ");
                       
                                              
    } // Ends Main
    
    
    /**  
     * Method to read the input file into an array of bytes
     * @param file
     * @return 
     */
    public static byte[] fileToBytes(File file)
    {
        byte[] inputBytes = new byte[(int) file.length()];
        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(inputBytes);
            fileInputStream.close();
            return inputBytes;
        }
        catch (Exception e)
        {           
            
            e.printStackTrace();
        }
        return null;
    }
    
    
    /**
     * Method simulates a packet drop by using a random number generator.
     * The user inputs a number 1-99
     * If the random number matches the user's number, the packet is dropped.
     * This method is run before each packet transmission.
     * @return 
     */
    private static boolean DropPacket(int userNum){
        //random number generator with current system time as the seed
        Random chance = new Random(System.currentTimeMillis());
        //binds the random value to between 0 and 99
        int rand = chance.nextInt(100);
                
        return (userNum == rand);
    } 
        
    
} //Ends Class 

