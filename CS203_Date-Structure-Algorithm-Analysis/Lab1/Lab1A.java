package Lab1;
import java.util.Scanner;

public class Lab1A {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			boolean same = false;
			int len1 = input.nextInt();
			String[] words = new String[len1];
			for (int j = 0; j < len1; j++) {
				words[j] = input.next();
			}
			int len2 = input.nextInt();
			String[] sen = new String[len2];
			for (int j = 0; j < len2; j++) {
				sen[j] = input.next();
			}
			for (String j : words) {
				for (String k : sen) {
					if (j.equalsIgnoreCase(k)) {
						same = true;
					}
				}
			}
			System.out.println(same ? "Appeared" : "Not appeared");
		}
	}
}
