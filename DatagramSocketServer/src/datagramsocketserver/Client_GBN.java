
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John 


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.apache.commons.io.FileUtils;

public class Client_GBN {

    public static void main(String[] args) throws Exception {

        int currentWindow = 0;
        int numACKPacketsSent = 0;
        int numACKPacketsLost = 0;
        int portNumServer = 50000;
        int portNumClient = 50001;
        int packetSize = 87;
        boolean emptyData = false;
        File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt");

        // Scanner obj for user input
        Scanner input = new Scanner(System.in);

        // prompt user for random number to simulate packet loss         
        int userInput;
        do {
            System.out.println(" Please enter a number between [0,99] (This should match the probability entered from the server. ");
            while (!input.hasNextInt()) {
                System.out.println("Input is not a number, please try again.");
                input.next();
            }
            userInput = input.nextInt();
        } while (userInput < 0 || userInput > 99);

        // setup the udp socket for the client to recieve data from the server
        DatagramSocket clientSocket = new DatagramSocket(portNumClient);
        byte[] tempBytes = new byte[packetSize];
        ArrayList<PacketData> received = new ArrayList<PacketData>();        

        // recieve data until there is no more
        while (!emptyData) {
            
            DatagramPacket datagramPacket = new DatagramPacket(tempBytes, tempBytes.length);
            clientSocket.receive(datagramPacket);
            PacketData datagramObject = (PacketData) toObject(datagramPacket.getData());

            // no more data left to send
            if (datagramObject.getSeq() == currentWindow && datagramObject.isLast()) {
                currentWindow++;
                received.add(datagramObject);
                emptyData = true;
            // packet recieved correctly 
            } else if (datagramObject.getSeq() == currentWindow) {
                currentWindow++;
                received.add(datagramObject);
            // transmission error 
            } else {
                // drop packet 
                
            }

            ReturnData ackInfo = new ReturnData(currentWindow);
            byte[] ackMessage = toBytes(ackInfo);
            DatagramPacket ackPacket = new DatagramPacket(
                    ackMessage,
                    ackMessage.length,
                    //datagramPacket.getAddress(),
                    //datagramPacket.getPort()
                    InetAddress.getLocalHost(),
                    portNumServer
            );
            
            if (DropACKPacket(userInput)) {
                numACKPacketsLost++;
            } else {
                clientSocket.send(ackPacket);
                numACKPacketsSent++;
            }

            // write data from recieved packet to the outputFile
            FileUtils.writeByteArrayToFile(outputFile, datagramPacket.getData(), true);
        }

        // Print the data received
        System.out.println(" ------------ DATA ---------------- ");

        for (PacketData p : received) {
            for (byte b : p.getData()) {
                System.out.print((char) b);
            }
        }

    } // End Main 

    /**
     * Method simulates a packet drop by using a random number generator. The
     * user inputs a number 1-99 If the random number matches the user's number,
     * the packet is dropped. This method is run before each acknowledgement
     * packet transmission.
     *
     * @return
     */
    private static boolean DropACKPacket(int userNum) {
        //random number generator with current system time as the seed
        Random chance = new Random(System.currentTimeMillis());
        //binds the random value to between 0 and 99
        int rand = chance.nextInt(100);

        return (userNum == rand);
    }

    /**
     * Method to convert our packet objects into bytes for sending to the client
     *
     * @param file
     * @return
     */
    public static byte[] toBytes(Object obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    /**
     * Method to convert a byte array to our packet object so that it can be
     * used by the server
     *
     * @param file
     * @return
     */
    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return o.readObject();
    }

    /**
     * This should be called at the very beginning of the app, so that if the
     * app is run more than once it clears the output file instead of constantly
     * appending data to the end
     */
    public static void outClear() {
        File outputFile = new File("src/datagramsocketserver/COSC635_P2_DataRecieved.txt");
        try {
            if (outputFile.exists()) {
                FileWriter clearer = new FileWriter(outputFile, false);
                //'false' means it should overwrite the contents of the file. I hope.
                clearer.write(" ");
                clearer.flush();
                clearer.close();
                System.out.print("Output file cleared");
            }
        } catch (IOException e) {

        }
    }

} // Ends Class
