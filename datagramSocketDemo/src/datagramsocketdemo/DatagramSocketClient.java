/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagramsocketdemo;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DatagramSocketClient 
{
    //Global GUI varribles
    
    //varrible for connect button
    public static JButton B_CONNECT = new JButton("CONNECT");
    
    //varriable for main application window
    public static JFrame MAIN_WINDOW = new JFrame("Datagram Packet Server");
    
    //varriable for scrollbar
    public static JScrollPane SP_OUTPUT = new JScrollPane();
    
    //varriable for text output area
    public static JTextArea TA_OUTPUT = new JTextArea();

    
//----------------------------------------------------------------------------//    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) 
    {
        
        BuildGui();

    }

//----------------------------------------------------------------------------//
    
    public static void Connect()
    {
        try
        {
         
            //create a datagram socket
            DatagramSocket SOCK = new DatagramSocket();
            
            //connect to the server
            byte[] BUFFER = new byte[1024];
            
            InetAddress IP_ADDRESS = InetAddress.getByName("localhost");
            
            DatagramPacket PACKET = new DatagramPacket(BUFFER, BUFFER.length, IP_ADDRESS, 777);

                SOCK.send(PACKET);

                //reconstruct bew Packet and request packet from server
                PACKET = new DatagramPacket(BUFFER, BUFFER.length);

                SOCK.receive(PACKET);

                //extract data from stream and convert to string
                String MESSAGE = new String(PACKET.getData(), 0, PACKET.getLength());
                
                //appends String "MESSAGE" to the global gui output varrible "TA_OUTPUT"
                TA_OUTPUT.append(MESSAGE);
            
            SOCK.close();
            
        }
        catch(IOException X)
        {
            System.out.print(X);
        }
    }
    
//----------------------------------------------------------------------------//

     /**
     *constructor for the GUI and sets the specific fields for the global GUI varribles
     */
    
    public static void BuildGui()
    {
        
        MAIN_WINDOW.setSize(2000, 1800);
        MAIN_WINDOW.setLocation(200, 200);
        MAIN_WINDOW.setResizable(true);
        MAIN_WINDOW.setBackground(new java.awt.Color(255, 255, 255));
        MAIN_WINDOW.getContentPane().setLayout(null);
        
        B_CONNECT.setBackground(new java.awt.Color(0, 0, 255));
        B_CONNECT.setForeground(new java.awt.Color(255, 255, 255));
        MAIN_WINDOW.getContentPane().add(B_CONNECT);
        B_CONNECT.setBounds(150, 200, 220, 50);
        
        TA_OUTPUT.setLineWrap(true);
        SP_OUTPUT.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_OUTPUT.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_OUTPUT.setViewportView(TA_OUTPUT);
        MAIN_WINDOW.getContentPane().add(SP_OUTPUT);
        SP_OUTPUT.setBounds(500, 45, 1000, 800);
        
        //instansiates the action listener method to allow for user mouse clicks
        MainWindow_Action();        
        
        MAIN_WINDOW.setVisible(true);
        
    }
    

//----------------------------------------------------------------------------//

    /**
     * creates a methods that implements ActionListener to be able to read user mouse clicks
     */
    
    public static void MainWindow_Action()
    {
        
        B_CONNECT.addActionListener(
        
        new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                Connect();
            }        
        });
    }      

    
    /**
     * This method takes our received data packet and writes it to an output file.
     * This will append the packet to the output file.
     * @param in is an array of received data;
     */
    public static void writeOut(byte[] in)
    {
        //Converts the byte[] back into a String.
        //I think you have to do this. 
        String message = new String(in);
        
        //Makes our file.
        File outputFile = new File("./635Group4Output.txt"); 
        
        //Gets a string to the directory location of the outputfile
        String location = outputFile.getAbsolutePath();
        
        //Tells user where to find the output file.
        System.out.println("Writing output to 635Group4Output.txt at: "
                + location);
        
        try 
        {
            //The true parameter means it will append data rather than overwrite the file..
            FileWriter outWriter = new FileWriter(outputFile, true);
            
            //Writes our output.
            outWriter.write(message);
            
            outWriter.flush();
            
            outWriter.close();
            
        } 
        
        catch (IOException e)
            
        {
            
            System.out.println("Output file wasn't found, this shouldn't be possible.");
            
        }
    }
    
    /**
     * This should be called at the very beginning of the app, so that if the app
     * is run more than once it clears the output file instead of constantly
     * appending data to the end
     */
    public static void outClear()
    {
        
        File outputFile = new File("./635Group4Output.txt"); 
        
        try 
        {
            
            if(outputFile.exists())
            {
                
                //'false' means it should overwrite the contents of the file. I hope.
                FileWriter clearer = new FileWriter (outputFile, false);
                
                clearer.write(" ");
                
                clearer.flush();
                
                clearer.close();
                
                System.out.print("Output file cleared");
                
            } 
        } 
        
        catch (IOException e)
            
        {
            
        }
    }
    
}

