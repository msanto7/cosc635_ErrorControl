
/// COSC 635 - Project #2 - SAW Protocol 
/// Group 4 
/// -- Mike Santoro 
/// -- Marisa Hammond
/// -- William Kealey
/// -- John Haney
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Server_GBN {

    public static void main(String[] args) throws Exception {

        int last = 0;
        int numPacketsSent = 0;
        int numPacketsLost = 0;
        int portNumServer = 50000;
        int portNumClient = 50001;
        int window = 2;
        int packetSize = 4;
        int seqACK = 0;
        long startTime = 0;
        long endTime = 0;

        // Scanner obj for user input
        Scanner input = new Scanner(System.in);

        InetAddress localhost = InetAddress.getLocalHost();

        System.out.println("Server Started.");
        System.out.println("IP address: " + localhost.getHostAddress().trim());
        System.out.println("Port Number: " + portNumServer);

        // prompt user for random number to simulate packet loss         
        int userInput;
        do {
            System.out.println(" Please enter a number between [0,99]. ");
            while (!input.hasNextInt()) {
                System.out.println("Input is not a number, please try again.");
                input.next();
            }
            userInput = input.nextInt();
        } while (userInput < 0 || userInput > 99);

        // Print user defined packet loss percentage
        System.out.println("Number " + userInput + " recieved. ");

        // read the input file into an array of bytes 
        File inputFile = new File("src/datagramsocketserver/COSC635_P2_DataSent.txt");
        byte[] inputBytes = fileToBytes(inputFile);

        // Last packet sequence number
        numPacketsSent = (int) Math.ceil((double) inputBytes.length / packetSize);

        DatagramSocket serverSocket = new DatagramSocket(portNumServer);
        InetAddress clientIP = InetAddress.getByName("localhost");

        // list of packet objects
        ArrayList<PacketData> packetList = new ArrayList<PacketData>();

        // keep server running 
        while (true) {

            // save the start time 
            startTime = System.nanoTime();

            // attempt to send data while input file still has text
            while (last - seqACK < window && last < numPacketsSent) {

                // create new byte array of the section we want for this specific packet
                byte[] filePacketBytes = new byte[packetSize];
                filePacketBytes = Arrays.copyOfRange(inputBytes, last * packetSize, last * packetSize + packetSize);
                // create packet object for sending
                PacketData serverPacket = new PacketData(last, filePacketBytes, (last == numPacketsSent - 1) ? true : false);
                byte[] sendData = toBytes(serverPacket);

                // create packet to send to the client 
                DatagramPacket packet = new DatagramPacket(
                        sendData,
                        sendData.length,
                        //clientIP,
                        InetAddress.getLocalHost(),
                        portNumClient
                );
                packetList.add(serverPacket);

                if (DropPacket(userInput)) {
                    //packet is dropped - update counter
                    numPacketsLost++;
                } else {
                    // send the udp packet to the client
                    serverSocket.send(packet);
                }
                last++;
            }

            // recieve ack
            byte[] buffer = new byte[40];
            DatagramPacket ack = new DatagramPacket(buffer, buffer.length);

            try {
                // resend data if timeout is reached befire ack is recieved 
                serverSocket.setSoTimeout(3000);
                serverSocket.receive(ack);
                ReturnData ackObject = (ReturnData) toObject(ack.getData());
                if (ackObject.getPacket() == numPacketsSent) {
                    break;
                }
                seqACK = Math.max(seqACK, ackObject.getPacket());

            } catch (SocketTimeoutException e) {

                for (int i = seqACK; i < last; i++) {
                    System.out.println("Acknowledgment not recieved after 3 seconds, resending data");
                    byte[] sendData = toBytes(packetList.get(i));

                    // create packet to send to the client 
                    DatagramPacket packet = new DatagramPacket(
                            sendData,
                            sendData.length,
                            //clientIP,
                            InetAddress.getLocalHost(),
                            portNumClient
                    );

                    if (DropPacket(userInput)) {
                        //packet is dropped - update counter
                        numPacketsLost++;
                    } else {
                        // send the udp packet to the client
                        serverSocket.send(packet);
                    }
                }
            }

        }

        // save stop time when sending data has finished 
        endTime = System.nanoTime();

        //Print out Basic Statistics
        System.out.println("The server took " + (endTime - startTime) + " nanoseconds to complete sending all of the data.");
        System.out.println("The server successfully sent " + numPacketsSent + " packets. ");
        System.out.println("The server dropped " + numPacketsLost + " packets (simulated). ");

    } // Ends main

    /**
     * Method to read the input file into an array of bytes
     *
     * @param file
     * @return
     */
    public static byte[] fileToBytes(File file) {
        byte[] inputBytes = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(inputBytes);
            fileInputStream.close();
            return inputBytes;
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
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
     * Method simulates a packet drop by using a random number generator. The
     * user inputs a number 1-99 If the random number matches the user's number,
     * the packet is dropped. This method is run before each packet
     * transmission.
     *
     * @return
     */
    private static boolean DropPacket(int userNum) {
        //random number generator with current system time as the seed
        Random chance = new Random(System.currentTimeMillis());
        //binds the random value to between 0 and 99
        int rand = chance.nextInt(100);

        return (userNum == rand);
    }

} // Ends class 
