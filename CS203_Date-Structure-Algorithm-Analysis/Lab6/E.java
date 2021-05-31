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
				int discard = 0;
				int M = in.nextInt();
				int N = in.nextInt();
				int capacity = 0;
				ArrayList<Integer> heaplist = new ArrayList<>();
				int[] number = new int[(int) (Math.pow(2, 20) + 1)];
				int[] history = new int[(int) (Math.pow(2, 20) + 1)];
				int[] indexlist = new int[(int) (Math.pow(2, 20) + 1)];
				for (int i = 0; i < N; i++) {
					int x = in.nextInt();
					if (capacity < M) {
						int exist = number[x];
						if (exist == 0) {
							heaplist.add(x);
							number[x] = 1;
							history[x] = i;
							indexlist[x] = heaplist.size() - 1;
							capacity++;
						} else {
							Updata(heaplist, number, history, indexlist, x);
						}
					} else {
						int exist = number[x];
						if (exist == 0) {
							heaplist.add(x);
							number[x] = 1;
							history[x] = i;
							indexlist[x] = 0;
							Delete(heaplist, number, history, indexlist);
							discard++;
						} else {
							Updata(heaplist, number, history, indexlist, x);
						}
					}
				}
				out.println(discard);
			}
		}

		public static void Updata(ArrayList<Integer> heaplist, int[] number, int[] history, int[] indexlist, int x) {
			int index = indexlist[x];
			number[x]++;
			while (index != 0) {
				int y = heaplist.get((index - 1) / 2);
				if (number[x] > number[y] || (number[x] == number[y] && history[x] < history[y])) {
					heaplist.set(index, y);
					heaplist.set((index - 1) / 2, x);
					indexlist[y] = index;
					indexlist[x] = (index - 1) / 2;
				}
				index = (index - 1) / 2;
			}
		}

		public static void Delete(ArrayList<Integer> heaplist, int[] number, int[] history, int[] indexlist) {
			number[heaplist.get(0)] = 0;
			history[heaplist.get(0)] = 0;
			indexlist[heaplist.get(0)] = 0;
			heaplist.set(0, heaplist.get(heaplist.size() - 1));
			heaplist.remove(heaplist.size() - 1);
			int index = 0;
			while (2 * index + 1 < heaplist.size()) {
				int x = heaplist.get(index);
				// have two sides
				if (2 * index + 2 < heaplist.size()) {
					int y1 = heaplist.get(2 * index + 1);
					int y2 = heaplist.get(2 * index + 2);
					if (number[y1] > number[y2] || (number[y1] == number[y2] && history[y1] < history[y2])) {
						if (number[x] < number[y1] || (number[x] == number[y1] && history[x] > history[y1])) {
							heaplist.set(index, y1);
							heaplist.set(2 * index + 1, x);
							indexlist[y1] = index;
							indexlist[x] = 2 * index + 1;
							index = 2 * index + 1;
							continue;
						} else {
							break;
						}
					} else {
						if (number[x] < number[y2] || (number[x] == number[y2] && history[x] > history[y2])) {
							heaplist.set(index, y2);
							heaplist.set(2 * index + 2, x);
							indexlist[y2] = index;
							indexlist[x] = 2 * index + 2;
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
					if (number[x] < number[y1] || (number[x] == number[y1] && history[x] > history[y1])) {
						heaplist.set(index, y1);
						heaplist.set(2 * index + 1, x);
						indexlist[y1] = index;
						indexlist[x] = 2 * index + 1;
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