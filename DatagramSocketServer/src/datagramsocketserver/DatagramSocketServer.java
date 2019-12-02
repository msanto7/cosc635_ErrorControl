/*
 * To use, the server must be running first before client prgram can be run
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

        //creates datagram scoket    
        protected DatagramSocket SOCK = null;

        public int PACKCOUNT =0;

        //calls method that reads from .txt file and returns elements as an array of strings              
        String[] bArray = readArray("../COSC635_P2_DataSent.txt"); 
        
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
        
        SOCK = new DatagramSocket(555);
        
        System.out.println("Launch server thread");
        
        System.out.println("Waiting for client to connect...");
        
    }
    
//----------------------------------------------------------------------------// 
    
    /***
     * initiates the program to be able to receive confirmation from client to begin sending packets
     */   
    
    public void run()
    {
      while(true) 
      {
          try
          {     
              //varriable to hold the data 
              byte[] BUFFER = new byte[1024];
              
              //Recieve client request
              DatagramPacket PACKET = new DatagramPacket(BUFFER, BUFFER.length);
              
              SOCK.receive(PACKET);
              
              //varrible created from the return of NextPack ie the lines read from .txt file
              String MESSAGE = NextPack();  
              
              //Loop to increase number of lines sent in each packet
              for (int i = 0;  i < 5; i++)
              {
                  MESSAGE = MESSAGE + NextPack();
              }    

              //"BUFFER" holds lines read as string, in byte array form
              BUFFER = MESSAGE.getBytes();
              
              //send reply to client at "address" and "port"              
              InetAddress IP_ADDRESS = PACKET.getAddress();
              
              //gets the port of the recieved packet
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
        
        //used to count number of strings   
        int counter = 0;
        
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
                   
           if(PACKCOUNT > bArray.length - 1) 
           {  
               
              MESSAGE = "\n out of elements to send finished at:\n" + theTime.toString() + " .";
              
              System.out.println("\nAll packets were sent");
              
           }
           else
           {
               
               MESSAGE = (PACKCOUNT + 1) + ". " + bArray[PACKCOUNT];
               
               System.out.println("\n sending out packet # " + (PACKCOUNT + 1));
               
               PACKCOUNT++;
               
           }
           
           return MESSAGE;
       }
    
}
