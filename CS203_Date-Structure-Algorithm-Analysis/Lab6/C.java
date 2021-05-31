package Lab6;

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

public class C {
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
				ArrayList<Integer> heaplist = new ArrayList<Integer>();
//				for (int i = 0; i < num; i++) {
//					heaplist.add(in.nextInt());
//				}
//				Collections.sort(heaplist);
				for (int i = 0; i < num; i++) {
					Add(heaplist, in.nextInt());
				}

				int q = in.nextInt();
				for (int i = 0; i < q; i++) {
					int op = in.nextInt();
					switch (op) {
					case 1:
						int x = in.nextInt();
						Add(heaplist, x);
						break;
					case 2:
						Delete(heaplist);
						break;
					case 3:
						out.println(heaplist.get(0));
						break;

					}
				}
			}
		}

		public static void Delete(ArrayList<Integer> heaplist) {
			heaplist.set(0, heaplist.get(heaplist.size() - 1));
			heaplist.remove(heaplist.size() - 1);
			int index = 0;
			while (2 * index + 1 < heaplist.size()) {
				// have two sides
				if (2 * index + 1 < heaplist.size() && 2 * index + 2 < heaplist.size()) {
					if (heaplist.get(2 * index + 1) <= heaplist.get(2 * index + 2)) {
						if (heaplist.get(index) > heaplist.get(2 * index + 1)) {
							int temp = heaplist.get(index);
							heaplist.set(index, heaplist.get(2 * index + 1));
							heaplist.set(2 * index + 1, temp);
							index = 2 * index + 1;
							continue;
						} else {
							break;
						}
					} else {
						if (heaplist.get(index) > heaplist.get(2 * index + 2)) {
							int temp = heaplist.get(index);
							heaplist.set(index, heaplist.get(2 * index + 2));
							heaplist.set(2 * index + 2, temp);
							index = 2 * index + 2;
							continue;
						} else {
							break;
						}
					}
				}
				// only left side
				if (2 * index + 1 < heaplist.size()) {
					if (heaplist.get(index) > heaplist.get(2 * index + 1)) {
						int temp = heaplist.get(index);
						heaplist.set(index, heaplist.get(2 * index + 1));
						heaplist.set(2 * index + 1, temp);
						index = 2 * index + 1;
						continue;
					} else {
						break;
					}
				}
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