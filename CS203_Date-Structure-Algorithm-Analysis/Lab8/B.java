package Lab8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
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
				int n = in.nextInt();
				int m = in.nextInt();
				ArrayList<Integer> graph[] = new ArrayList[n];
				for (int i = 0; i < graph.length; i++) {
					graph[i] = new ArrayList<Integer>();
				}
				for (int i = 0; i < m; i++) {
					int node1 = in.nextInt() - 1;
					int node2 = in.nextInt() - 1;
					graph[node1].add(node2);
				}
				int Q = in.nextInt();
				for (int i = 0; i < Q; i++) {
					Queue<Integer> queue = new LinkedList<>();
					int start = in.nextInt() - 1;
					int end = in.nextInt() - 1;
					queue.offer(start);
					boolean isout = false;
					boolean ischeck[] = new boolean[n];
					while (!queue.isEmpty()) {
						int node = queue.poll();
						if (node == end) {
							out.println("YES");
							isout = true;
							break;
						} else {
							for (int j = 0; j < graph[node].size(); j++) {
								if (ischeck[graph[node].get(j)] == false) {
									queue.offer(graph[node].get(j));
									ischeck[graph[node].get(j)] = true;
								}
							}
						}
					}
					if (!isout) {
						out.println("NO");
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