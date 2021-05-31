package Lab1;
import java.util.Scanner;

public class Lab1C {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			int a = input.nextInt();
			int b = input.nextInt();
			System.out.println(a == 1 & b == 1 ? "Bob" : "Alice");
		}
	}
}