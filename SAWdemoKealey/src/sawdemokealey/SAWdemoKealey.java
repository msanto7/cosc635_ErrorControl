/*
 * Playing with the Java networking classes. Hopefully some of this code is useful. 
 */
package sawdemokealey;
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.*;
//I don't know what will and won't be useful so we can trim this list of imports later.
/**
 *
 * @author wrkealey0
 * @version 1115191603
 */
public class SAWdemoKealey {
    private static final int WINSIZE = 15;
    // 15 is an arbitrary window size for sending/recieving data.
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        try{
            File inputFile = new File("../COSC635_P2_DataSent.txt");
            Scanner sysReader = new Scanner(inputFile);
        /**
            while(sysReader.hasNext()){
            System.out.println(sysReader.next());
            
            } 
         //Makes sure files can be read properly, ignore me, I always do this.
         */
        
        
            //Scrapped the queue idea, opting instead to more efficently just collect every byte of data to transmit.       
            //again, better solutions are welcomed!
            ArrayList<Byte> bytesList = new ArrayList<Byte>();
            while(sysReader.hasNext()){
                byte[] n = sysReader.next().getBytes();
                for (byte b : n){
                    bytesList.add(b);
                }
            }
            
            System.out.println(bytesList.get(WINSIZE));
  
            //Adds file data to queue. The reason for using a Queue is to ensure
            //data transmission follows FIFO and cannot skip around.
            
            DatagramSocket intSocket = new DatagramSocket(); 
            //By using param 0 we set it to find a socket for us.
            System.out.println("Found available socket at: "+intSocket.getLocalPort()); //Works!
            //Just printing some routine information.
            System.out.println("Local address: "+intSocket.getLocalSocketAddress());
            System.out.println("'Recieve' Buffersize (.getReceiveBufferSize()): "+intSocket.getReceiveBufferSize());
           // Socket accSocket = intSocket.accept();
     
            
            //Output streams appear to transmit byte code.
            
            //Converts the input data into an array of bytes for data packets. I recommend breaking this down.
            byte[] bytes = new byte[bytesList.size()];
            for (int i = 0; i<bytesList.size(); i++){
                bytes[i] = bytesList.get(i);
            }
        
            
           
            
            DatagramPacket dPack = new DatagramPacket(bytes, WINSIZE); //Again, maybe we break this down into smaller arrays of bytes.
   
            
        } catch (IOException t){
            System.out.println("Something about servers, ports, and sockets - oh my!");
        }
        
    }
    
}

