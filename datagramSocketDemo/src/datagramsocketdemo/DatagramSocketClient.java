/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagramsocketdemo;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DatagramSocketClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        DatagramSocket skt = null;
        //creates the DatagramSocket
        try{
            
            skt = new DatagramSocket();
            
            String[] bArray = readArray("../COSC635_P2_DataSent.txt");
            //calls method that reads from .txt file and returns elements as an array of strings
            
            String holder = "";
            
            //little bit of code to print elements in bArray
//            for (int i = 0;  i < bArray.length; i++) {
//                System.out.println(bArray[i]);
//                holder = bArray[i];
//                
//            }
            
            String msg = "text message";
            //test message this is where the file reader varible could be put instead
            
            byte [] b = msg.getBytes();
            //converts data into array of bytes and holds it in varrible "b"
            
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
    /***
     * takes in a .txt file and creates an array of strings of the elements in the file
     * @param String file
     * @return Byte[] byteSize
     */
    public static String[] readArray(String file) {
        
        int counter = 0;
        
        try{
            Scanner s = new Scanner(new File(file));
            while (s.hasNextLine()){
                counter++;
                s.next();
            }
            
            String[] words = new String[counter];
            
            Scanner s1 = new Scanner(new File(file));
            
            for (int i = 0; i < counter; i++) {
                words[i] = s1.next();
            }
            return words;
        }
        catch(FileNotFoundException e){
            System.out.println("file not found");
        }
        return null;
    } 
    
    
    /**
     * This method takes our received data packet and writes it to an output file.
     * This will append the packet to the output file.
     * @param in is an array of received data;
     */
    public static void writeOut(byte[] in){
        String message = new String(in);
        //Converts the byte[] back into a String.
        //I think you have to do this. 
        File outputFile = new File("./635Group4Output.txt"); 
        //Makes our file.
        String location = outputFile.getAbsolutePath();
        //Gets a string to the directory location of the outputfile
        System.out.println("Writing output to 635Group4Output.txt at: "
                + location);
        //Tells user where to find the output file.
        try {
            FileWriter outWriter = new FileWriter(outputFile, true);
            //The true parameter means it will append data rather than overwrite the file..
            outWriter.write(message);
            //Writes our output.
            outWriter.flush();
            outWriter.close();
        } catch (IOException e){
            System.out.println("Output file wasn't found, this shouldn't be possible.");
        }
    }
    
    /**
     * This should be called at the very beginning of the app, so that if the app
     * is run more than once it clears the output file instead of constantly
     * appending data to the end
     */
    public static void outClear(){
        File outputFile = new File("./635Group4Output.txt"); 
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
    
}
