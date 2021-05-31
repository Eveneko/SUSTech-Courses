package Lab1;
import java.util.Arrays;
import java.util.Scanner;

public class Lab1G {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			int[] rec = new int[3];
			for (int j = 0; j < 3; j++) {
				rec[j] = input.nextInt();
			}
			Arrays.sort(rec);
			int a = rec[2];
			int b = rec[1];
			int c = rec[0];
			int x = input.nextInt();
			int y = input.nextInt();
			boolean form = false;
			if (Math.max(x, y) >= Math.max(2 * b + 2 * c, a + 2 * c)
					& Math.min(x, y) >= Math.min(2 * b + 2 * c, a + 2 * c)) {
				form = true;
			}
			if (Math.max(x, y) >= Math.max(2 * a + 2 * c, b + 2 * c)
					& Math.min(x, y) >= Math.min(2 * a + 2 * c, b + 2 * c)) {
				form = true;
			}
			if (Math.max(x, y) >= 3 * a + b + c & Math.min(x, y) >= b + c) {
				form = true;
			}
			if (Math.max(x, y) >= a + 3 * b + c & Math.min(x, y) >= a + c) {
				form = true;
			}
			if (Math.max(x, y) >= a + b + 3 * c & Math.min(x, y) >= a + b) {
				form = true;
			}
			if (Math.max(x, y) >= a + 2 * b + c & Math.min(x, y) >= a + 2 * c) {
				form = true;
			}
			if (Math.max(x, y) >= a + b + 2 * c & Math.min(x, y) >= a + b + c) {
				form = true;
			}
			System.out.println(form ? "Yes" : "No");
		}
	}
}
