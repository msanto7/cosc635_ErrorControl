
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney

package datagramsocketserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
        
        // run method to clear the outputfile if it exists
        outClear();
              
        try (DatagramSocket clientSocket = new DatagramSocket(portNumClient)) {
            byte[] tempBytes = new byte[1024];
            File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt");
            
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(tempBytes, tempBytes.length);
                clientSocket.receive(datagramPacket);
                
                // write data from recieved packet to the outputFile
                FileUtils.writeByteArrayToFile(outputFile, datagramPacket.getData(), true);
                
                String ackMessage = "received packet";
                
                // create packet to send to the client 
                DatagramPacket ackPacket = new DatagramPacket(
                        ackMessage.getBytes(),
                        ackMessage.length(),
                        InetAddress.getLocalHost(),
                        portNumServer
                );
                
                // send acknowledgement packet to the server
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
    
       
    /**
     * This should be called at the very beginning of the app, so that if the app
     * is run more than once it clears the output file instead of constantly
     * appending data to the end
     */
    public static void outClear(){
        File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt"); 
        try {
            if(outputFile.exists()){
                FileWriter clearer = new FileWriter (outputFile, false);
                //'false' means it should overwrite the contents of the file. I hope.
                clearer.write(" ");
                clearer.flush();
                clearer.close();
                System.out.print("Output file cleared");
            } 
        } catch (IOException e){
            
        }
    }
    
} // Ends class 
