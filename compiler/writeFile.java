package compiler;

import java.io.*;

/**
 * Class to read the byte arrays
 * 
 * @author Saaki Vishnumolakala
 * @version 1.0
 * Compiler Assignment 4
 * CS322 - Compiler Construction
 * Spring 2023
 */
public class writeFile {
    public static void writeFile(byte [] byteArray, String fileName)
    {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(byteArray);
            out.close();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
