package Lab1;
import java.util.Scanner;

public class Lab1B {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			int n = input.nextInt();
			long num = 1;
			for (int j = 0; j < n; j++) {
				num = 3 * num;
				num = num % 1000000007;
			}
			System.out.println(num - 1 >= 0 ? num - 1 : num - 1 + 1000000007);
		}
	}
}