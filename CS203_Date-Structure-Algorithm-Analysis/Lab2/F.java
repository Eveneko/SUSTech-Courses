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
public class F {
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
				int N = in.nextInt();
				double[] x = new double[N];
				double[] w = new double[N];
				for (int j = 0; j < N; j++) {
					x[j] = in.nextDouble();
					w[j] = in.nextDouble();
				}
				double L = x[0];
				double R = x[N - 1];
				double eps = 1e-12;
				while (Math.abs(L - R) > eps) {
					double mid = (L + R) / 2;
					double mmid = (mid + R) / 2;
					if (check(mid, mmid, x, w)) {
						R = mmid;
					} else {
						L = mid;
					}
				}
				double total = 0;
				for (int j = 0; j < N; j++) {
					double S = Math.abs(x[j] - L);
					total += S * S * S * w[j];
				}
				out.printf("Case #%d: %.0f\n", i + 1, total);
			}
		}

		public static boolean check(double mid, double mmid, double[] x, double[] w) {
			double total1 = 0;
			for (int i = 0; i < w.length; i++) {
				double S = Math.abs(x[i] - mid);
				total1 += S * S * S * w[i];
			}
			double total2 = 0;
			for (int i = 0; i < w.length; i++) {
				double S = Math.abs(x[i] - mmid);
				total2 += S * S * S * w[i];
			}
			if (total1 < total2) {
				return true;
			} else {
				return false;
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
