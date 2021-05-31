package Lab2;

import java.util.Scanner;

public class D {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while (in.hasNext()) {
			int days = in.nextInt();
			int orders = in.nextInt();
			long[] list = new long[days];
			for (int i = 0; i < days; i++) {
				list[i] = in.nextLong();
			}
			long[] rooms = new long[orders];
			int[] start = new int[orders];
			int[] end = new int[orders];
			for (int i = 0; i < orders; i++) {
				rooms[i] = in.nextLong();
				start[i] = in.nextInt();
				end[i] = in.nextInt();
			}
			int L = 0;
			int R = orders;
			while (L < R) {
				int mid = (L + R) / 2;
				System.out.println("L:" + L + " R:" + R + " M:" + mid);
				if (check(mid, days, orders, list, rooms, start, end)) {
					L = mid;
				} else {
					R = mid;
				}
			}
			if (L > orders) {
				System.out.println(0);
			} else {
				System.out.printf("-1\n%d", L + 1);
				break;
			}
		}
	}

	public static boolean check(int x, int days, int orders, long[] list, long[] rooms, int[] start, int[] end) {
		Scanner in = new Scanner(System.in);
		int total = 0;
		int[] num = new int[days + 1];
		for (int i = 0; i < num.length; i++) {
			num[i] = 0;
		}
		for (int i = 0; i < x; i++) {
			num[start[i] - 1] += rooms[i];
			num[end[i]] -= rooms[i];
		}
		for (int i = 0; i < orders; i++) {
			total += num[i];
			if (total > list[i]) {
				return false;
			}
		}
		return true;
	}
}