package Lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in Actual solution is at the top Author: Wavator
 */
public class A {
	public static void main(String[] args) {
		InputStream inputStream = System.in;// new FileInputStream("C:\\Users\\wavator\\Downloads\\test.in");
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	public static int check(int L, int n, int[] ple, int mid) {
		int ans = 0;
		int step = 0;
		for (int i = 0; i < n; i++) {
			while (i < n & (ple[i] - step) < mid) {
				i++;
			}
			ans++;
			step = ple[i];
		}
		return ans;
	}

	static class Task {

		public void solve(InputReader in, PrintWriter out) {
			while (in.hasNext()) {
				// the number of books
				int num = in.nextInt();
				// stop the program with the 0
				if (num == 0) {
					break;
				}
				// make a list to store whether the letter appears
				int[] list = new int[26];
				String str = in.next();
				int count = 0;
				int leave = 0;
				// 0 = have not borrow or back
				// 1 = borrowing
				// 2 = no book to read
				for (int i = 0; i < str.length(); i++) {
					int char1 = (int) str.charAt(i) - 65;
					if (list[char1] == 2) {
						continue;
					}
					if (list[char1] == 0) {
						list[char1] = 1;
						count++;
						if (count > num) {
							leave++;
							count--;
							list[char1] = 2;
						}
					} else {
						list[char1] = 0;
						count--;
					}
				}
				out.println(leave);
			}
		}
	}

	static class InputReader {
		public BufferedReader reader;
		public StringTokenizer tokenizer;

		public InputReader(InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream), 32768);
			tokenizer = null;
		}

		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public char[] nextCharArray() {
			return next().toCharArray();
		}

		// public boolean hasNext() {
		// try {
		// return reader.ready();
		// } catch(IOException e) {
		// throw new RuntimeException(e);
		// }
		// }
		public boolean hasNext() {
			try {
				String string = reader.readLine();
				if (string == null) {
					return false;
				}
				tokenizer = new StringTokenizer(string);
				return tokenizer.hasMoreTokens();
			} catch (IOException e) {
				return false;
			}
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecinal() {
			return new BigDecimal(next());
		}
	}
}