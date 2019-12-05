
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney

package datagramsocketserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import org.apache.commons.io.FileUtils;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class Client_SAW {       
    
    public static void main(String[] args)
    {       
        int portNumServer = 50000;        
        int portNumClient = 50001;
              
        try (DatagramSocket clientSocket = new DatagramSocket(portNumClient)) {
            byte[] tempBytes = new byte[1024];
            File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt");
            
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(tempBytes, tempBytes.length);
                clientSocket.receive(datagramPacket);
                
//                try (FileOutputStream output = new FileOutputStream("filename", true)) {
//                    output.write(data);
//                }
                // write bytes to the end of our output file                         
                FileUtils.writeByteArrayToFile(outputFile, datagramPacket.getData(), true);
                
                String ackMessage = "received packet";
                
                // create packet to send to the client 
                DatagramPacket ackPacket = new DatagramPacket(
                        ackMessage.getBytes(),
                        ackMessage.length(),
                        InetAddress.getLocalHost(),
                        portNumServer
                );
                clientSocket.send(ackPacket);        
            }                        
        }
        catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
               
    } // Ends Main
       
    
    // **** method to take an array of bytes and write it to an output file
    public static void bytesToFile(byte[] bytes)
    {
        
    }
    
} // Ends class 
