package Lab2;

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
public class B {
	public static void main(String[] args) {
		InputStream inputStream = System.in;// new FileInputStream("C:\\Users\\wavator\\Downloads\\test.in");
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(InputReader in, PrintWriter out) {
			int times = in.nextInt();
			for (int i = 0; i < times; i++) {
				long y = in.nextLong();
				double left = 0;
				double right = 100;
				double mid = (left + right) / 2;
				double eps = Math.pow(10.0, -8.0);
				// F(x) = 5x^7+6x^6+3x^3+4x^2-2xy (0 <= x <=100)
				double ds = 35 * Math.pow(mid, 6.0) + 36 * Math.pow(mid, 5.0) + 9 * Math.pow(mid, 2.0) + 8 * mid
						- 2 * y;
				double result = 5 * Math.pow(mid, 7.0) + 6 * Math.pow(mid, 6.0) + 3 * Math.pow(mid, 3.0)
						+ 4 * Math.pow(mid, 2.0) - 2 * mid * y;
				while (Math.abs(left - right) > eps) {
					if (ds >= 0) {
						right = mid;
					}
					if (ds < 0) {
						left = mid;
					}
					mid = (left + right) / 2;
					ds = 35 * Math.pow(mid, 6.0) + 36 * Math.pow(mid, 5.0) + 9 * Math.pow(mid, 2.0) + 8 * mid - 2 * y;
					result = 5 * Math.pow(mid, 7.0) + 6 * Math.pow(mid, 6.0) + 3 * Math.pow(mid, 3.0)
							+ 4 * Math.pow(mid, 2.0) - 2 * mid * y;
				}
				out.printf("Case " + (i + 1) + ": " + "%.4f\n", result);
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