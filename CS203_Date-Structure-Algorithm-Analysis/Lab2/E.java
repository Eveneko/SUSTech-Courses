package Lab2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in Actual solution is at the top Author: Wavator
 */
public class E {
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
				int L = in.nextInt();
				int n = in.nextInt();
				int m = in.nextInt();
				int[] ple = new int[n + 1];
				for (int i = 0; i < n; i++) {
					ple[i] = in.nextInt();
				}
				ple[n] = L;
				Arrays.sort(ple);
				int min = 0;
				int max = L;
				int mid = (min + max) / 2;
				while (min <= max) {
					mid = (min + max) / 2;
					int S = check(L, n, ple, mid);
					out.println("min:" + min + " mid:" + mid + " max:" + max + " S:" + S + " m:" + m);
					if (S >= m) {
						min = mid + 1;
					} else {
						max = mid - 1;
					}
				}

				out.println(mid);
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