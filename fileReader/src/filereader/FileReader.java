/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filereader;

import java.io.*;
import java.util.Scanner;

public class FileReader {


    public static void main(String[] args) {
        
            File msg1 = new File("../test.txt");
            Scanner sysReader = new Scanner(File(msg1));
            byte [] inputByteArray = new byte[200000];
            int i = 0;
            
            while (sysReader.hasNextByte()) {
                inputByteArray[i] = sysReader.nextByte();
                i++;
            }
        
    }
    
}
