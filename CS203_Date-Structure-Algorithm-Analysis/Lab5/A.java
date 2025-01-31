package Lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class A {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(InputReader in, PrintWriter out) {
			for (int t = in.nextInt(); t > 0; t--) {
				int N = in.nextInt();
				int[] list = new int[26];
				int num = 0;
				char ch = '0';
				for (int i = 0; i < N; i++) {
					String string = in.next();
					char ch1 = string.charAt(string.length() - 1);
					if (ch == '0') {
						ch = ch1;
						num++;
						if (list[((int) ch) - ((int) 'a')] < num) {
							list[((int) ch) - ((int) 'a')] = num;
						}
					} else if (ch == ch1) {
						num++;
						if (list[((int) ch) - ((int) 'a')] < num) {
							list[((int) ch) - ((int) 'a')] = num;
						}
					} else if (ch != ch1) {
						num = 1;
						ch = ch1;
						if (list[((int) ch) - ((int) 'a')] < num) {
							list[((int) ch) - ((int) 'a')] = num;
						}
					}
				}
				int max = 0;
				for (int i = 0; i < 26; i++) {
					max = max > list[i] ? max : list[i];
				}
				out.print(max);
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