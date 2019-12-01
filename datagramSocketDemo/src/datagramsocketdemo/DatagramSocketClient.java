/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagramsocketdemo;

import java.awt.Color;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class DatagramSocketClient 
{
    //Global GUI varribles
    public static JButton B_CONNECT = new JButton("CONNECT");
    public static JFrame MainWindow = new JFrame("Datagram Packet Server");
    public static JScrollPane SP_OUTPUT = new JScrollPane();
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
            
            DatagramPacket PACKET = new DatagramPacket(BUFFER, BUFFER.length, IP_ADDRESS, 444);

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
        
        MainWindow.setSize(2000, 1800);
        MainWindow.setLocation(200, 200);
        MainWindow.setResizable(true);
        MainWindow.setBackground(new java.awt.Color(255, 255, 255));
        MainWindow.getContentPane().setLayout(null);
        
        B_CONNECT.setBackground(new java.awt.Color(0, 0, 255));
        B_CONNECT.setForeground(new java.awt.Color(255, 255, 255));
        MainWindow.getContentPane().add(B_CONNECT);
        B_CONNECT.setBounds(150, 200, 220, 50);
        
        TA_OUTPUT.setLineWrap(true);
        SP_OUTPUT.setHorizontalScrollBarPolicy(
                  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SP_OUTPUT.setVerticalScrollBarPolicy(
                  ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SP_OUTPUT.setViewportView(TA_OUTPUT);
        MainWindow.getContentPane().add(SP_OUTPUT);
        SP_OUTPUT.setBounds(500, 45, 1000, 800);
        
        MainWindow_Action();
        //instansiates the action listener method to allow for user mouse clicks
        
        MainWindow.setVisible(true);
        
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
}