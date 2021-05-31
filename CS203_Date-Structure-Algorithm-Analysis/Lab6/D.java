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

public class D {
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
		static int longest;
		static ArrayList<Integer> Nodes[] = new ArrayList[100000];
		static int[] first = new int[100000];
		static int[] second = new int[100000];

		public void solve(InputReader in, PrintWriter out) {
			for (int t = in.nextInt(); t > 0; t--) {
				int len = in.nextInt();
				for (int i = 0; i < 100000; i++) {
					ArrayList<Integer> list = new ArrayList<>();
					Nodes[i] = list;
				}
				// creat the tree by Arraylisy-List
				for (int i = 1; i < len; i++) {
					int num1 = in.nextInt();
					int num2 = in.nextInt();
					Nodes[num2].add(num1);
					Nodes[num1].add(num2);
				}

				// test
//				for (int i = 0; i < len; i++) {
//					for (int j = 0; j < Nodes[i].size(); j++) {
//						out.print(Nodes[i].get(j) + " ");
//					}
//					out.println();
//				}

				// search
				search(1, 0, len);
				out.println(longest);
				longest = 0;
				first = new int[100000];
				second = new int[100000];
			}
		}

		public static void search(int now, int pre, int len) {
			len = Nodes[now].size();
			for (int i = 0; i < len; i++) {
				if (Nodes[now].get(i) == pre) {
					continue;
				}
				search(Nodes[now].get(i), now, Nodes[Nodes[now].get(i)].size());
				if (first[Nodes[now].get(i)] + 1 > first[now]) {
					second[now] = first[now];
					first[now] = first[Nodes[now].get(i)] + 1;
				} else if (first[Nodes[now].get(i)] + 1 > second[now]) {
					second[now] = first[Nodes[now].get(i)] + 1;
				}
			}
			longest = Math.max(first[now] + second[now], longest);
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