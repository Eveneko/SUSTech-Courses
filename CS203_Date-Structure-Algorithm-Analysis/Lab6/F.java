package Lab6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class F {
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
				int num = in.nextInt();
				int[] bw = new int[num + 1];
				for (int i = 1; i < num + 1; i++) {
					bw[i] = in.nextInt();
				}
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
				}
				Queue<Integer> queue = new LinkedList<>();
				queue.offer(1);
				int count = 0;
				int[] OE = new int[(int) (Math.log(num) / Math.log(2)) + 1];
				while (!queue.isEmpty()) {
					int nood = queue.poll();
					count++;
					if (nood == -1) {
						continue;
					}
					if (bw[nood] % 2 == 1) {
						OE[(int) (Math.log(count) / Math.log(2))]++;
					}
					if (L[nood] != 0) {
						queue.offer(L[nood]);
					} else {
						queue.offer(-1);
					}
					if (R[nood] != 0) {
						queue.offer(R[nood]);
					} else {
						queue.offer(-1);
					}
				}

				if (OE[0] == 0) {
					for (int i = 1; i < OE.length; i++) {
						if (OE[i] % 2 == 1) {
							out.println("YES");
							break;
						}
						if (i == OE.length - 1) {
							out.println("NO");
						}
					}
				} else {
					boolean iszero = true;
					for (int i = 1; i < OE.length; i++) {
						if (OE[i] != 0) {
							iszero = false;
							break;
						}
					}
					if (iszero) {
						out.println("YES");
						continue;
					}
					for (int i = 1; i < OE.length; i++) {
						if (OE[i] % 2 == 1) {
							out.println("YES");
							break;
						}
						if (i == OE.length - 1) {
							out.println("NO");
						}
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