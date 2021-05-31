package Lab4;

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

/**
 * Built using CHelper plug-in Actual solution is at the top Author: Wavator
 */
public class C {
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
			for (int t = in.nextInt(); t > 0; t--) {
				int n = in.nextInt();
				long m = in.nextLong();
				long count = 0;
				long[] list = new long[n];
				for (int i = 0; i < list.length; i++) {
					list[i] = in.nextLong();
				}
				// BlockingQueue<Long> queue = new ArrayBlockingQueue<Long>(n);
				Queue<Long> queue = new LinkedList<>();
				for (int i = 0; i < list.length; i++) {

					long num = list[i];

					if (i == 0) {
						queue.offer(num);
					}

					if (i != 0 && i != n - 1) {
						while (num - queue.peek() > m) {
							if (queue.size() >= 3) {
								count += (queue.size() - 1) * (queue.size() - 2) / 2;
							}

							queue.poll();
						}
						if (num - queue.peek() <= m) {
							queue.offer(num);
						}
					}

					if (i == n - 1) {
						while (!queue.isEmpty() && num - queue.peek() > m) {
							if (queue.size() >= 3) {
								count += (queue.size() - 1) * (queue.size() - 2) / 2;
							}
							queue.poll();
						}
						queue.offer(num);
						while (!queue.isEmpty() && num - queue.peek() <= m) {
							if (queue.size() >= 3) {
								count += (queue.size() - 1) * (queue.size() - 2) / 2;
							}
							queue.poll();
						}
					}

				}
				out.println(count);
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