package Lab7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class E {
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
				int n = in.nextInt();
				int k = in.nextInt();
				ArrayList<Integer> heaplist = new ArrayList<Integer>();
				for (int i = 0; i < k; i++) {
					Add(heaplist, in.nextInt());
				}
				for (int i = 0; i < n - k; i++) {
					int x = in.nextInt();
					if (x > heaplist.get(0)) {
						out.print(heaplist.get(0) + " ");
						Update(heaplist, x);
					} else {
						out.print(x + " ");
					}
				}
				for (int i = 0; i < k - 1; i++) {
					out.print(heaplist.get(0) + " ");
					Update(heaplist, 1000000001);
				}
				out.println(heaplist.get(0));
			}
		}

		public static void Add(ArrayList<Integer> heaplist, int x) {
			heaplist.add(x);
			int index = heaplist.size() - 1;
			while ((index - 1) / 2 >= 0) {
				if (x < heaplist.get((index - 1) / 2)) {
					heaplist.set(index, heaplist.get((index - 1) / 2));
					heaplist.set((index - 1) / 2, x);
					index = (index - 1) / 2;
				} else {
					break;
				}
			}
		}

		public static void Update(ArrayList<Integer> heaplist, int x) {
			heaplist.set(0, x);
			int index = 0;
			while (2 * index + 1 < heaplist.size()) {
				x = heaplist.get(index);
				// have two sides
				if (2 * index + 2 < heaplist.size()) {
					int y1 = heaplist.get(2 * index + 1);
					int y2 = heaplist.get(2 * index + 2);
					if (y1 < y2) {
						if (x > y1) {
							heaplist.set(index, y1);
							heaplist.set(2 * index + 1, x);
							index = 2 * index + 1;
							continue;
						} else {
							break;
						}
					} else {
						if (x > y2) {
							heaplist.set(index, y2);
							heaplist.set(2 * index + 2, x);
							index = 2 * index + 2;
							continue;
						} else {
							break;
						}
					}
				}

				// only left side
				if (2 * index + 1 < heaplist.size()) {
					int y1 = heaplist.get(2 * index + 1);
					if (x > y1) {
						heaplist.set(index, y1);
						heaplist.set(2 * index + 1, x);
						index = 2 * index + 1;
						continue;
					} else {
						break;
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