/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagramsocketdemo;

import java.io.*;
import java.net.*;

public class DatagramSocketClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DatagramSocket skt = null;
        //creates the DatagramSocket
        try{
            
            skt = new DatagramSocket();
            
            String msg = "text message";
            //test message this is where the file reader varible could be put instead
            
            byte [] b = msg.getBytes();
            //converts data into string of bytes and holds it in varrible "b"
            
            InetAddress host = InetAddress.getByName("localhost");
            //internet address of server "localhost" is local machine
            
            int serverSocket = 6700;
            //socket or port number to be used
            
            DatagramPacket request = new DatagramPacket(b, b.length , host, serverSocket);
            //creates datagram packet puts in data "b", with length.b, host ip address, port number
            
            skt.send(request);
            //initializes socket "skt" to send packet "request"
            
            
            //---------------------------------------------------//
            
            byte [] buffer = new byte[1024];
            //creates buffer to catch and hold incoming packet data
            
            DatagramPacket reply = new DatagramPacket(buffer,buffer.length);
            //constructs packet to hold incoming byte data to be read on client machine
            
            skt.receive(reply);
            //initializes socket to recieve incoming packet data
            
            System.out.println("Client recieved " + new String(reply.getData()));
            
            skt.close();
            //need to close socket at end of transmission
        }
        catch(Exception ex){
            
        }
    }
    
}
