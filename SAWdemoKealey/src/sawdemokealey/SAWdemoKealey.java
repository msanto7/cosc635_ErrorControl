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
            Queue<String> fileData = new LinkedList<>();
            while(sysReader.hasNext()){
                fileData.add(sysReader.next()); 
            }
            //Adds file data to queue. The reason for using a Queue is to ensure
            //data transmission follows FIFO and cannot skip around.
        try{//Begins nested try block, keeps our error messages consistent.
            ServerSocket intSocket = new ServerSocket(0); 
            //By using param 0 we set it to find a socket for us.
            System.out.println("Found available socket at: "+intSocket.getLocalPort()); //Works!
            Socket accSocket = intSocket.accept();
            //Ties up the socket and waits for incoming client. 
            //We'll need this in both ends of the application to recieve data or the signal to send more data.
            OutputStream accOutput = accSocket.getOutputStream();
            //Output streams appear to transmit byte code.
            int counter = 1;
            //Counter keeps track of how many cells of the queue are sent
            while(fileData.peek()!=null&&counter<WINSIZE){
                accOutput.write(fileData.element().getBytes());
                counter++;
                //Reads and transmits data until it hits WINSIZE-1, stops at WINSIZE-1 to await confirmation
                //From here we can reset the counter when the confirmation returns.
            }
            
        } catch (IOException t){
            System.out.println("Something about servers, ports, and sockets - oh my!");
        }
        } catch (FileNotFoundException e){
            System.out.println("Input file is not found.");
        }
        
    }
    
}
