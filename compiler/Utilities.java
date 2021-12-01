/**
 * 
 * Description: Used to write the file using the bytearray and file name
 * 
 * @author Justin Mattix
 * @author David Jones
 * @author Taden Duerod
 * @version 1.0
 * Programming Project Four
 * CS-322 Compiler Construction
 * Fall 2021
 * 
 */
package compiler;

import java.io.*;

public class Utilities {
	public static void writeFile(byte[] bytearray, String fileName) {
		try {
			FileOutputStream out = new FileOutputStream(fileName);
			out.write(bytearray);
			out.close();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
