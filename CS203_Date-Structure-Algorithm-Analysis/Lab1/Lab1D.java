package Lab1;
import java.util.Scanner;

public class Lab1D {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			int a = input.nextInt();
			int b = input.nextInt();
			int c = input.nextInt();
			char[][] D3 = new char[2 * a + 2 * b + 1][2 * b + 2 * c + 1];
			for (int j = 0; j < 2 * b + 2 * c + 1; j++) {
				for (int k = 0; k < 2 * a + 2 * b + 1; k++) {
					D3[k][j] = '.';
				}
			}
			// print the front face
			for (int x = 0; x < 2 * c + 1; x++) {
				if (x % 2 == 0) {
					for (int j = 0; j < 2 * a + 1; j++) {
						if (j % 2 == 0) {
							D3[j][2 * b + x] = '+';
						} else {
							D3[j][2 * b + x] = '-';
						}
					}
				} else {
					for (int j = 0; j < 2 * a + 1; j++) {
						if (j % 2 == 0) {
							D3[j][2 * b + x] = '|';
						} else {
							D3[j][2 * b + x] = '.';
						}
					}
				}
			}
			for (int l = 0; l < 2 * a + 1; l++) {
				if (l % 2 == 0) {
					for (int k = 0; k < 2 * b + 1; k++) {
						if (k % 2 == 0) {
							D3[2 * a + 2 * b - k - l][k] = '+';
						} else {
							D3[2 * a + 2 * b - k - l][k] = '/';
						}
					}
				} else {
					for (int k = 0; k < 2 * b + 1; k++) {
						if (k % 2 == 0) {
							D3[2 * a + 2 * b - k - l][k] = '-';
						} else {
							D3[2 * a + 2 * b - k - l][k] = '.';
						}
					}
				}
			}
			for (int l = 0; l < 2 * c + 1; l++) {
				if (l % 2 == 0) {
					for (int k = 0; k < 2 * b + 1; k++) {
						if (k % 2 == 0) {
							D3[2 * a + 2 * b - k][k + l] = '+';
						} else {
							D3[2 * a + 2 * b - k][k + l] = '/';
						}
					}
				} else {
					for (int k = 0; k < 2 * b + 1; k++) {
						if (k % 2 == 0) {
							D3[2 * a + 2 * b - k][k + l] = '|';
						} else {
							D3[2 * a + 2 * b - k][k + l] = '.';
						}
					}
				}
			}
			for (int j = 0; j < 2 * b + 2 * c + 1; j++) {
				for (int k = 0; k < 2 * a + 2 * b + 1; k++) {
					System.out.print(D3[k][j]);
				}
				System.out.println("");
			}
		}
	}
}