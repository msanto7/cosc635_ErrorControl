/*
 * To use server must be running first before client prgram can be run
 * 
 * 
 */
package datagramsocketserver;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Scanner;

public class DatagramSocketServer extends Thread 
{
    
        protected DatagramSocket SOCK = null;
        //creates datagram scoket
        public int packCount =0;
        
        String[] bArray = readArray("../COSC635_P2_DataSent.txt");
        //calls method that reads from .txt file and returns elements as an array of strings       
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        
        DatagramSocketServer SERVER = new DatagramSocketServer();
        SERVER.start();

    }
    
//----------------------------------------------------------------------------//
    
     /***
     *defining elements of the class constructor
     */ 
    public DatagramSocketServer() throws IOException
    {
        SOCK = new DatagramSocket(444);
        System.out.println("Launch server thread");
        System.out.println("Waiting for client to connect...");
    }
    
//----------------------------------------------------------------------------// 
    
    /***
     * initiates the program to be able to receive confirmation to begin sending packets
     */   
    
    public void run()
    {
      while(true) 
      {
          try
          {
              byte[] BUFFER = new byte[1024];
              
              DatagramPacket PACKET = new DatagramPacket(BUFFER, BUFFER.length);
              //Recieve client request
              
              SOCK.receive(PACKET);
              
              String MESSAGE = NextPack();
              
              BUFFER = MESSAGE.getBytes();
              
              InetAddress IP_ADDRESS = PACKET.getAddress();
              //send reply to client at "address" and "port"
              
              int PORT = PACKET.getPort();
              
              PACKET = new DatagramPacket(BUFFER, BUFFER.length, IP_ADDRESS, PORT);
              
              SOCK.send(PACKET);
          }
          catch(IOException e)
          {
              e.printStackTrace();
              break;
          }
      }
    }
    
//----------------------------------------------------------------------------//  
    
    /***
     * takes in a .txt file and creates an arrayList of the elements in the file
     * @param String file
     * @return ArrayList byteList
     */ 
       public static String[] readArray(String file) 
       {
           
        int counter = 0;
        //used to count number of strings
        
        try
        {
            Scanner s1 = new Scanner(new File(file));
            
            while (s1.hasNextLine()) 
            {
                
                counter++;
                
                s1.nextLine();
                                
            }
            String[] wordList = new String[counter];
            
            Scanner s2 = new Scanner(new File(file));
            
            for (int i = 0; i < counter; i++) 
            {
                
                String line = s2.nextLine();
                
                wordList[i] = line + "\n";

            }
            return wordList;
        }
        catch (FileNotFoundException e) 
        {
            
        }
        return null;
    }

//----------------------------------------------------------------------------//

    /***
     * takes in a .txt file and creates an arrayList of the elements in the file
     * @param String file
     * @return ArrayList byteList
     */        
       public String NextPack (){
 
           String MESSAGE = "";
           Date theTime = new Date();
                   
           if(packCount > bArray.length - 1) 
           {  
              MESSAGE = "\n out of elements to send finished at:\n" +
                      theTime.toString() + " .";
              System.out.println("\nAll packets were sent");
           }
           else
           {
               MESSAGE = (packCount + 1) + ". " + bArray[packCount];
               System.out.println("\n sending out packet # " + (packCount + 1));
               packCount++;
           }
           
           return MESSAGE;
       }
    
}
