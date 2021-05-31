package Lab6;

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
				// build tree
				int num = in.nextInt();
				int[] F = new int[num + 1];
				int[] L = new int[num + 1];
				int[] R = new int[num + 1];
				for (int i = 0; i < num - 1; i++) {
					int num1 = in.nextInt();
					int num2 = in.nextInt();
					if (L[num1] == 0) {
						L[num1] = num2;
					} else {
						R[num1] = num2;
					}
					F[num2] = num1;
				}

				int root = 0;
				for (int i = 1; i < num + 1; i++) {
					if (F[i] == 0) {
						root = i;
						break;
					}
				}
				// the pre order
				int[] count1 = new int[num + 1];
				int roottimes = 0;
				int nood = root;
				while (roottimes < 3) {
					if (nood == root) {
						roottimes++;
					}
					count1[nood]++;
					if (count1[nood] == 1) {
						out.print(nood + " ");
					}
					if (L[nood] != 0 && count1[L[nood]] == 0) {
						nood = L[nood];
					} else if (R[nood] != 0 && count1[R[nood]] == 0) {
						nood = R[nood];
					} else {
						nood = F[nood];
					}
				}
				out.println();
				// in order
				int[] count2 = new int[num + 1];
				roottimes = 0;
				nood = root;
				while (roottimes < 3) {
					if (nood == root) {
						roottimes++;
					}
					count2[nood]++;
					if (L[nood] == 0) {
						count2[nood] += 1;
					}
					if (count2[nood] == 2) {
						out.print(nood + " ");
					}
					if (L[nood] != 0 && count2[L[nood]] == 0) {
						nood = L[nood];
					} else if (R[nood] != 0 && count2[R[nood]] == 0) {
						nood = R[nood];
					} else {
						nood = F[nood];
					}
				}
				out.println();

				// post order
				int[] count3 = new int[num + 1];
				roottimes = 0;
				nood = root;
				for (int i = 1; i < num + 1; i++) {
					if (L[i] == 0) {
						count3[i] += 2;
					} else if (R[i] == 0) {
						count3[i] += 1;
					}
				}
				while (roottimes < 3) {
					if (nood == root) {
						roottimes++;
					}
					count3[nood]++;
					if (count3[nood] == 3) {
						out.print(nood + " ");
					}
					if (L[nood] != 0 && count3[L[nood]] != 3) {
						nood = L[nood];
					} else if (R[nood] != 0 && count3[R[nood]] != 3) {
						nood = R[nood];
					} else {
						nood = F[nood];
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