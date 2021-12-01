/**
 * 
 * Description: Used to generate ASM code using asmify
 * 
 * @author Justin Mattix
 * @author David Jones
 * @author Taden Duerod
 * @version 13.0
 * Programming Project 4
 * CS322 - Compiler Construction
 * Fall 2021
 *
 */
package compiler;

import java.util.Scanner;

public class testing {
	public static void main(String args[]) {
		int x;
		
		Scanner s = new Scanner(System.in);
		System.out.println("Enter a number: ");
		x = s.nextInt();
		while(x < 0) {
			System.out.println(x);
			x = x - 1;
		}
	}
}
