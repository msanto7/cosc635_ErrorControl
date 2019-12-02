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
}