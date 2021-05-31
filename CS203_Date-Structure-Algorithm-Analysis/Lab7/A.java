package Lab7;

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

public class A {
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
				// build binary tree with parent and two children
				int[][] binarytree = new int[n + 1][3];
				for (int i = 1; i < binarytree.length; i++) {
					binarytree[i][1] = in.nextInt();
					binarytree[i][2] = in.nextInt();
					binarytree[binarytree[i][1]][0] = i;
					binarytree[binarytree[i][2]][0] = i;
				}
				// find the root
				int root = 0;
				for (int i = 1; i < binarytree.length; i++) {
					if (binarytree[i][0] == 0) {
						root = i;
						break;
					}
				}
				// judge
				Queue<Integer> queue = new LinkedList<>();
				queue.offer(root);
				while (!queue.isEmpty()) {
					int nood = queue.poll();
					if (binarytree[nood][1] != 0) {
						queue.offer(binarytree[nood][1]);
					} else {
						if (binarytree[nood][2] != 0) {
							out.println("No");
							break;
						}
						boolean isout = false;
						while (!queue.isEmpty()) {
							int leaf = queue.poll();
							if (binarytree[leaf][1] != 0 || binarytree[leaf][2] != 0) {
								out.println("No");
								isout = true;
								break;
							}
						}
						if (!isout) {
							out.println("Yes");
							break;
						} else {
							break;
						}
					}
					if (binarytree[nood][2] != 0) {
						queue.offer(binarytree[nood][2]);
					} else {
						boolean isout = false;
						while (!queue.isEmpty()) {
							int leaf = queue.poll();
							if (binarytree[leaf][1] != 0 || binarytree[leaf][2] != 0) {
								out.println("No");
								isout = true;
								break;
							}
						}
						if (!isout) {
							out.println("Yes");
							break;
						} else {
							break;
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