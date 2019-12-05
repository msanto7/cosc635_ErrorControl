/*
 * To use, the server must be running first before client prgram can be run
 * 
 * 
 */
package datagramsocketserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class DatagramSocketServer extends Thread 
{

    //creates datagram scoket    
    protected DatagramSocket SOCK = null;

    public int PACKCOUNT =0;

    //calls method that reads from .txt file and returns elements as an array of strings              
    String[] bArray = readArray("../COSC635_P2_DataSent.txt"); 
    
    //varrible for the window
    public static JFrame PSUEDO_WINDOW = new JFrame("Psudeo error loss simulator"); 
    
    //varriable for enter number label        
    public static JLabel TEXT_LABEL = new JLabel();
    
    //varriable for empty label which will show event after button clicked        
    public static JLabel ENTERED_LABEL = new JLabel();
    
    //varriable "textfield" to enter name        
    public static JTextField TEXT_FIELD = new JTextField();
    
    //varriable "INPUT" to take in user input in psuedo window
    public static String INPUT = new String("");
    
    //varriable BUTTON for submit button on psuedo window
    public static JButton BUTTON = new JButton("Submit");  
    
    //varriable to hold the percentage number set for packet loss
    public static int RANDOM;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        
        Window_Scanner();
        
        DatagramSocketServer SERVER = new DatagramSocketServer();
        
        SERVER.start();

    }
    
//----------------------------------------------------------------------------//
    
     /***
     *defining elements of the class constructor
     */ 
    public DatagramSocketServer() throws IOException
    {
        
        SOCK = new DatagramSocket(777);
        
        System.out.println("Launch server thread");
        
        System.out.println("Waiting for client to connect...");
        
    }
    
//----------------------------------------------------------------------------// 
    
    /***
     * initiates the program to be able to receive confirmation from client to begin sending packets
     */   
    
    public void run()
    {
      while(true) 
      {
          try
          {     
              //varriable to hold the data 
              byte[] BUFFER = new byte[1024];
              
              //Recieve client request
              DatagramPacket PACKET = new DatagramPacket(BUFFER, BUFFER.length);
              
              SOCK.receive(PACKET);
              
              for (int i = 0; i < bArray.length ; i++)
              {
                      
              //varrible created from the return of NextPack ie the lines read from .txt file
              String MESSAGE = NextPack();  
              
              //Loop to increase number of lines sent in each packet
              for (int j = 0;  j < 5; j++)
              {
                  
                  MESSAGE = MESSAGE + NextPack();
                  
              }    

              //"BUFFER" holds lines read as string, in byte array form
              BUFFER = MESSAGE.getBytes();
              
              //send reply to client at "address" and "port"              
              InetAddress IP_ADDRESS = PACKET.getAddress();
              
              //gets the port of the recieved packet
              int PORT = PACKET.getPort();
              
              PACKET = new DatagramPacket(BUFFER, BUFFER.length, IP_ADDRESS, PORT);
              
              if (dropSimulator(RANDOM) == false)
              {    
                  
                  SOCK.send(PACKET);
              
              }
              else
              {
                  
                  
                  
              }
              }
          }
          
          catch(IOException e)
              
          {
              
              e.printStackTrace();
              
              break;
              
          }
      }
    }
    
//----------------------------------------------------------------------------//  
    
    /***
     * takes in a .txt file and creates an arrayList of the elements in the file
     * @param String file
     * @return ArrayList byteList
     */ 
       public static String[] readArray(String file) 
       {
        
        //used to count number of strings   
        int counter = 0;
        
        try
        {
            Scanner s1 = new Scanner(new File(file));
            
            while (s1.hasNextLine()) 
            {
                
                counter++;
                
                s1.nextLine();
                                
            }
            String[] wordList = new String[counter];
            
            Scanner s2 = new Scanner(new File(file));
            
            for (int i = 0; i < counter; i++) 
            {
                
                String line = s2.nextLine();
                
                wordList[i] = line + "\n";

            }
            return wordList;
        }
        catch (FileNotFoundException e) 
        {
            
        }
        return null;
    }

//----------------------------------------------------------------------------//

    /***
     * takes in a .txt file and creates an arrayList of the elements in the file
     * @param String file
     * @return ArrayList byteList
     */        
       public String NextPack (){
 
           String MESSAGE = "";
           Date theTime = new Date();
                   
           if(PACKCOUNT > bArray.length - 1) 
           {  
               
              MESSAGE = "\n out of elements to send finished at:\n" + theTime.toString() + " .";
              
              System.out.println("\nAll packets were sent");
              
           }
           else
           {
               
               MESSAGE = (PACKCOUNT + 1) + ". " + bArray[PACKCOUNT];
               
               System.out.println("\n sending out packet # " + (PACKCOUNT + 1));
               
               PACKCOUNT++;
               
           }
           
           return MESSAGE;
       }
       
//----------------------------------------------------------------------------//    
  
     /**
     * Method simulates a packet drop by using a random number generator.
     * The user inputs a number 1-99
     * If the random number is less than the user's number, the packet is dropped.
     * This method is run before each packet transmission.
     * @return boolean
     */
    
    private static Boolean dropSimulator(int userNum)
    {

        //random number generator with current system time as the seed
        Random chance = new Random(System.currentTimeMillis());

        //binds the random value to between 0 and 99
        int rand = chance.nextInt(100);

        if (userNum < rand)
        {

            return false; //TODO make it plug in packet sequence number?   

        }
        else
        {

            return true;

        }
    }
    
//----------------------------------------------------------------------------//

    public static void Window_Scanner()
    {
        
	BUTTON.setBounds(400, 400, 140, 40);   
        	
	TEXT_LABEL.setText("Enter number between 1-99 :");
	TEXT_LABEL.setBounds(220, 150, 200, 100);
     
	ENTERED_LABEL.setBounds(220, 110, 200, 100);
       
	TEXT_FIELD.setBounds(220, 250, 500, 30);
        
	//add to frame
	PSUEDO_WINDOW.add(ENTERED_LABEL);
	PSUEDO_WINDOW.add(TEXT_FIELD);
	PSUEDO_WINDOW.add(TEXT_LABEL);
	PSUEDO_WINDOW.add(BUTTON);    
	PSUEDO_WINDOW.setSize(1000, 1000);    
	PSUEDO_WINDOW.setLayout(null);    
	PSUEDO_WINDOW.setVisible(true);    
	PSUEDO_WINDOW.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
	
        PsuedoWindow_Action();
    }

//----------------------------------------------------------------------------//

    /**
     * creates a methods that implements ActionListener to be able to enter number
     * to be used in psuedo packet drop simulator
     */    
    
    public static void PsuedoWindow_Action()
    {
        //action listener to enter number by pressing "enter" on keyboard to submit
        TEXT_FIELD.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                INPUT = TEXT_FIELD.getText();

                int userNum = Integer.parseInt(INPUT);

                //checks that number input is in valid range
                if ((userNum <0) || (userNum >99))
                {
                    
                    RANDOM = Integer.parseInt(INPUT);
                    
                    ENTERED_LABEL.setText("Please enter a number between 0-99: ");

                }
                else
                {
                    
                    RANDOM = Integer.parseInt(INPUT);
                    
                    ENTERED_LABEL.setText("packet loss is set to " + INPUT + "%");

                }


            }        
        });


        //action listener to enter number clicking on button to submit
        BUTTON.addActionListener(new ActionListener() 
        {

            @Override
            public void actionPerformed(ActionEvent arg0) 
            {

                INPUT = TEXT_FIELD.getText();

                int userNum = Integer.parseInt(INPUT);

                //checks that number input is in valid range
                if ((userNum <0) || (userNum >99))
                {

                    ENTERED_LABEL.setText("Please enter a number between 0-99: ");


                }
                else
                {

                    ENTERED_LABEL.setText("packet loss is set to " + INPUT + "%");

                }

            }          
        });
    }    
    
}
