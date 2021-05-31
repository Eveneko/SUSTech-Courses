package Lab1;
import java.util.Scanner;

public class Lab1E {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int times = input.nextInt();
		for (int i = 0; i < times; i++) {
			int num = input.nextInt();
			int[] nums = new int[num];
			for (int j = 0; j < num; j++) {
				nums[j] = input.nextInt();
			}
			int maxnum = nums[0];
			int maxdiff = nums[0] - nums[1];
			for (int j = 1; j < num; j++) {
				maxdiff = Math.max(maxdiff, maxnum - nums[j]);
				maxnum = Math.max(maxnum, nums[j]);
			}
			System.out.println(maxdiff);
		}
	}
}
