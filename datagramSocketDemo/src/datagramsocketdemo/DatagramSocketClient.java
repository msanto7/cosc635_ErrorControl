/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagramsocketdemo;

import java.io.*;
import java.net.*;
import java.util.Scanner;
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
    public static JFrame MainWindow = new JFrame("Datagram Packet Server");
    
    //varriable for scrollbar
    public static JScrollPane SP_OUTPUT = new JScrollPane();
    
    //varriable for text output area
    public static JTextArea TA_OUTPUT = new JTextArea();
    
    //varrible for the window
    public static JFrame FRAME = new JFrame("Psudeo error loss simulator"); 
    
    //varriable for enter name label        
    public static JLabel LABEL = new JLabel();
    
    //varriable for empty label which will show event after button clicked        
    public static JLabel LABEL1 = new JLabel();
    
    //varriable "textfield" to enter name        
    public static JTextField TEXTFIELD = new JTextField();
    
//----------------------------------------------------------------------------//    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) 
    {
        //creates window that asks user to enter number (1-99) to simulate packet loss
        Window_Scanner();
        
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
            
            DatagramPacket PACKET = new DatagramPacket(BUFFER, BUFFER.length, IP_ADDRESS, 555);

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
        
        //instansiates the action listener method to allow for user mouse clicks
        MainWindow_Action();        
        
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

//----------------------------------------------------------------------------//

    public static void Window_Scanner()
    {
        
        Scanner KEYBOARD = new Scanner(System.in);
        
	JButton BUTTON = new JButton("Submit");    
	BUTTON.setBounds(400, 400, 140, 40);   
        	
	LABEL.setText("Enter number between 1-99 :");
	LABEL.setBounds(220, 150, 200, 100);
     
	LABEL1.setBounds(10, 110, 200, 100);
       
	TEXTFIELD.setBounds(220, 250, 500, 30);
        
	//add to frame
	FRAME.add(LABEL1);
	FRAME.add(TEXTFIELD);
	FRAME.add(LABEL);
	FRAME.add(BUTTON);    
	FRAME.setSize(1000, 1000);    
	FRAME.setLayout(null);    
	FRAME.setVisible(true);    
	FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	
        TEXTFIELD.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String INPUT = TEXTFIELD.getText();
                LABEL1.setText(INPUT);
                
            }        
        });
        
        
	//action listener
	BUTTON.addActionListener(new ActionListener() 
        {
	        
		@Override
		public void actionPerformed(ActionEvent arg0) 
                {
                    
                    LABEL1.setText("packet loss is set to " + INPUT);	
                    
		}          
        });
	}
}