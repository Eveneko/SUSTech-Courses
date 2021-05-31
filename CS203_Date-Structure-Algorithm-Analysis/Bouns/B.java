package Bouns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class B {
	public static void main(String[] args) throws Exception {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(InputReader in, PrintWriter out) throws Exception {
			for (int t = in.nextInt(); t > 0; t--) {
				int len = in.nextInt();
				int k = in.nextInt();
				int[] list = new int[len];
				for (int i = 0; i < len; i++) {
					list[i] = in.nextInt();
				}
				int low = 0;
				int high = len - 1;
				int i = low;
				int j = high;
				boolean get = false;
				while (!get) {
					int key = list[i];
					low = i;
					high = j;
					while (i < j) {
						while (i < j && list[j] >= key) {
							j--;
						}
						while (i < j && list[i] <= key) {
							i++;
						}
						if (i < j) {
							int tem = list[i];
							list[i] = list[j];
							list[j] = tem;
						}
					}
					if (i == j) {
						list[low] = list[i];
						list[i] = key;
					}
					if (k == i + 1) {
						get = true;
						out.println(list[i]);
						break;
					} else if (k < i + 1) {
						j--;
						i = low;
					} else {
						i++;
						j = high;
					}
				}
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