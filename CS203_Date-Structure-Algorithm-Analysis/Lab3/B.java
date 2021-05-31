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
				int step1 = in.nextInt();
				int[] c1 = new int[step1];
				int[] e1 = new int[step1];
				for (int j = 0; j < step1; j++) {
					c1[j] = in.nextInt();
					e1[j] = in.nextInt();
				}
				int step2 = in.nextInt();
				int[] c2 = new int[step2];
				int[] e2 = new int[step2];
				for (int j = 0; j < step2; j++) {
					c2[j] = in.nextInt();
					e2[j] = in.nextInt();
				}

				int pointer1 = 0;
				int pointer2 = 0;
				boolean isfirst = true;

				while (pointer1 < step1 || pointer2 < step2) {
					int c = 0;
					int e = 0;
					if (pointer1 == step1 || pointer2 == step2) {
						if (pointer1 == step1) {
							c = c2[pointer2];
							e = e2[pointer2];
							pointer2++;
						} else {
							c = c1[pointer1];
							e = e1[pointer1];
							pointer1++;
						}
					} else {
						if (e1[pointer1] == e2[pointer2]) {
							c = c1[pointer1] + c2[pointer2];
							e = e1[pointer1];
							pointer1++;
							pointer2++;
						} else if (e1[pointer1] > e2[pointer2]) {
							c = c2[pointer2];
							e = e2[pointer2];
							pointer2++;
						} else if (e1[pointer1] < e2[pointer2]) {
							c = c1[pointer1];
							e = e1[pointer1];
							pointer1++;
						}
					}
					if (isfirst) {
						if (c == 0) {
							continue;
						} else if (c == -1 & e == 0) {
							out.print(-1);
							isfirst = false;
						} else if (c == -1 & e == 1) {
							out.print("-x");
							isfirst = false;
						} else if (c == -1 & e != 0 & e != 1) {
							out.print("-x^" + e);
							isfirst = false;
						} else if (c == 1 & e == 0) {
							out.print(1);
							isfirst = false;
						} else if (c == 1 & e == 1) {
							out.print("x");
							isfirst = false;
						} else if (c == 1 & e != 0 & e != 1) {
							out.print("x^" + e);
							isfirst = false;
						} else if (c != 0 & c != 1 & e == 0) {
							out.print(c);
							isfirst = false;
						} else if (c != 0 & c != 1 & e == 1) {
							out.print(c + "x");
							isfirst = false;
						} else {
							out.print(c + "x^" + e);
							isfirst = false;
						}
					} else {
						if (c == 0) {
							continue;
						} else if (c == -1 & e == 0) {
							out.print(-1);
						} else if (c == -1 & e == 1) {
							out.print("-x");
						} else if (c == -1 & e != 0 & e != 1) {
							out.print("-x^" + e);
						} else if (c == 1 & e == 0) {
							out.print("+" + 1);
						} else if (c == 1 & e == 1) {
							out.print("+x");
						} else if (c == 1 & e != 0 & e != 1) {
							out.print("+" + "x^" + e);
						} else if (c != 0 & c != 1 & e == 0) {
							out.print(c > 0 ? "+" + c : c);
						} else if (c != 0 & c != 1 & e == 1) {
							out.print(c > 0 ? "+" + c + "x" : c + "x");
						} else {
							out.print(c > 0 ? "+" + c + "x^" + e : c + "x^" + e);
						}
					}

				}
				out.println(isfirst ? 0 : "");
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