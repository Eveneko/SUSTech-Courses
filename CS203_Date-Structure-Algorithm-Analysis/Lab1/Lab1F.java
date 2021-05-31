package Lab1;
import java.util.Scanner;

public class Lab1F {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			String word = input.next();
			int[][] graph = new int[19][19];
			char[] list = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'x',
					'z' };
			for (int j = 0; j < word.length() - 1; j++) {
				if (getIndex(list, word.charAt(j)) != -1 & getIndex(list, word.charAt(j + 1)) != -1) {
					graph[getIndex(list, word.charAt(j))][getIndex(list, word.charAt(j + 1))]++;
				}
			}
			int num = 0;
			for (int j = 0; j < Math.pow(2, 19); j++) {
				int tran = 0;
				String num2 = binaryToDecimal(j);
				for (int k = 0; k < 19; k++) {
					for (int l = 0; l < 19; l++) {
						if (num2.charAt(k) != num2.charAt(l)) {
							tran += graph[k][l];
						}
					}
				}
				num = num >= tran ? num : tran;
			}
			System.out.println(num);
		}
	}

	public static int getIndex(char[] arr, char value) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == value) {
				return i;
			}
		}
		return -1;
	}

	public static String binaryToDecimal(int n) {
		String str = "";
		while (n != 0) {
			str = n % 2 + str;
			n = n / 2;
		}
		while (str.length() < 19) {
			str = '0' + str;
		}
		return str;
	}
}
