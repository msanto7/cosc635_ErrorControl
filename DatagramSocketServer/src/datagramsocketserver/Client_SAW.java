
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney
package datagramsocketserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import org.apache.commons.io.FileUtils; // requires the attached folder and project property
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

//Client - Receiver
public class Client_SAW {
    
    public static void main(String[] args) throws Exception {
        int portNumServer = 50000;
        int portNumClient = 50001;
        long startTime = 0;
        long endTime = 0;
        boolean runClient = true;
        int numACKPacketsSent = 0;

        // run method to clear the outputfile if it exists
        outClear();
        try (DatagramSocket clientSocket = new DatagramSocket(portNumClient)) {
            byte[] tempBytes = new byte[1024];
            File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt");

            while (runClient) {
                DatagramPacket datagramPacket = new DatagramPacket(tempBytes, tempBytes.length);

                // resend data if timeout is reached befire ack is recieved 
                clientSocket.setSoTimeout(20000);

                try {
                    clientSocket.receive(datagramPacket);
                } catch (SocketTimeoutException e) {
                    System.exit(0);
                }

                // save the start time 
                startTime = System.nanoTime();

                // write data from recieved packet to the outputFile
                FileUtils.writeByteArrayToFile(outputFile, datagramPacket.getData(), true);

                String ackMessage = "received packet";

                // create packet to send to the client 
                DatagramPacket ackPacket = new DatagramPacket(
                        ackMessage.getBytes(),
                        ackMessage.length(),
                        InetAddress.getLocalHost(),
                        //InetAddress.getByName("10.0.0.44"),
                        portNumServer
                );

                // send acknowledgement packet to the server
                clientSocket.send(ackPacket);
                
                numACKPacketsSent++;
            }

            // save stop time when sending data has finished 
            endTime = System.nanoTime();

            //Print out Basic Statistics
            System.out.println("The client took " + (endTime - startTime) + " nanoseconds to complete sending all of the data.");
            System.out.println("The client successfully sent " + numACKPacketsSent + "  ACK packets. ");

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    } // Ends Main

    /**
     * This should be called at the very beginning of the app, so that if the
     * app is run more than once it clears the output file instead of constantly
     * appending data to the end
     */
    public static String outClear() {
        String msg = "";
        File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt");
        try {
            if (outputFile.exists()) {
                FileWriter clearer = new FileWriter(outputFile, false);
                //'false' means it should overwrite the contents of the file. I hope.
                clearer.write("");
                clearer.flush();
                clearer.close();
                msg = "Output file cleared";
            }
        } catch (IOException e) {

        }
        return msg;
    }

} // Ends class 
