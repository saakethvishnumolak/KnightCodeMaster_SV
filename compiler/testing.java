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
