/*
 * To use server must be running first before client prgram can be run
 * 
 * 
 */
package datagramsocketserver;

import java.io.*;
import java.net.*;

public class DatagramSocketServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DatagramSocket skt = null;
        //creates datagram scoket
        
        try{
            skt = new DatagramSocket(6700);
            //creates socket with port number "6700" as is defined on client side
            
            byte [] buffer = new byte[1024];
            //creates byte array as a buffer to hold incoming data
            
            while(true) {
                
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                // creates datagram packet out of incoming buffered data
                
                skt.receive(request);
                //initializes socket to recieve the packet "request"
                
                String [] arrayMsg = (new String(request.getData())).split(" ");
                //holds packet data in an array of strings
                
                byte [] sendMsg = (arrayMsg[0]+" server processed").getBytes();
                //creates and acknowledgement data set with array element + ack in byte array
                
                DatagramPacket reply = new DatagramPacket(sendMsg, sendMsg.length, request.getAddress(),request.getPort());
                //creates acknowledgement packet
                
                skt.send(reply);
                //initializes socket to send acknowledgement to client
            }
        }
        catch(Exception ex){
            
        }
    }
    
}
