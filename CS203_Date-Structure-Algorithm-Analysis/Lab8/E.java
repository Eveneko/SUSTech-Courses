package Lab8;

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
				int m = in.nextInt();
				grid[][] graph = new grid[n][m];
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						grid newgrid = new grid(i, j, in.nextInt() - 1, false);
						graph[i][j] = newgrid;
					}
				}
				int cnt = 0;
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						Queue<grid> queue = new LinkedList<>();
						queue.offer(graph[i][j]);
						if (graph[i][j].iscolored) {
							continue;
						}
						cnt++;
						graph[i][j].iscolored = true;
						while (!queue.isEmpty()) {
							grid g = queue.poll();
							if (g.x - 1 >= 0 && !graph[g.x - 1][g.y].iscolored
									&& graph[g.x - 1][g.y].color == g.color) {
								queue.add(graph[g.x - 1][g.y]);
								graph[g.x - 1][g.y].iscolored = true;
							}

							if (g.x + 1 < n && !graph[g.x + 1][g.y].iscolored && graph[g.x + 1][g.y].color == g.color) {
								queue.add(graph[g.x + 1][g.y]);
								graph[g.x + 1][g.y].iscolored = true;
							}

							if (g.y - 1 >= 0) {
								if (!graph[g.x][g.y - 1].iscolored && graph[g.x][g.y - 1].color == g.color) {
									queue.add(graph[g.x][g.y - 1]);
									graph[g.x][g.y - 1].iscolored = true;
								}
							} else {
								if (!graph[g.x][m - 1].iscolored && graph[g.x][m - 1].color == g.color) {
									queue.add(graph[g.x][m - 1]);
									graph[g.x][m - 1].iscolored = true;
								}
							}

							if (g.y + 1 < n) {
								if (!graph[g.x][g.y + 1].iscolored && graph[g.x][g.y + 1].color == g.color) {
									queue.add(graph[g.x][g.y + 1]);
									graph[g.x][g.y + 1].iscolored = true;
								}
							} else {
								if (!graph[g.x][0].iscolored && graph[g.x][0].color == g.color) {
									queue.add(graph[g.x][0]);
									graph[g.x][0].iscolored = true;
								}
							}

						}
					}
				}
				out.println(cnt);
			}
		}
	}

	public static class grid {
		public int x;
		public int y;
		public int color;
		public boolean iscolored;

		public grid(int x, int y, int color, boolean iscolored) {
			this.x = x;
			this.y = y;
			this.color = color;
			this.iscolored = iscolored;
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