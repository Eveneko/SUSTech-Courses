package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class G_bl {
	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		Reader in = new Reader();
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(Reader in, PrintWriter out) throws IOException {
			for (int t = in.nextInt(); t > 0; t--) {
				int n = in.nextInt();
				int m = in.nextInt();
				int k = in.nextInt();
				ArrayList<Node> citys[] = new ArrayList[n];
				for (int i = 0; i < citys.length; i++) {
					citys[i] = new ArrayList<>();
				}
				for (int i = 0; i < m; i++) {
					int u = in.nextInt() - 1;
					int v = in.nextInt() - 1;
					int w = in.nextInt();
					Node pNode1 = new Node(v, w);
					citys[u].add(pNode1);
					Node pNode2 = new Node(u, w);
					citys[v].add(pNode2);
				}
				int[] mid = new int[k];
				for (int i = 0; i < k; i++) {
					mid[i] = in.nextInt() - 1;
				}
				boolean[] isout = new boolean[k];
				long[] dist = new long[n];
				long result = Long.MAX_VALUE;
				long[][] dij = new long[k][n];
				for (int i = 0; i < k; i++) {
					for (int j = 0; j < dist.length; j++) {
						dist[j] = Long.MAX_VALUE;
					}
					dijkstra(mid[i], dist, citys, n);
					for (int j = 0; j < n; j++) {
						dij[i][j] = dist[j];
					}
				}
				for (int i = 0; i < dist.length; i++) {
					dist[i] = Long.MAX_VALUE;
				}
				dist = dijkstra(0, dist, citys, n);
				switch (k) {
				case 1:
					for (int a = 0; a < k; a++) {

						long tmp = 0;
						tmp += dist[mid[a]];
						tmp += dij[a][n - 1];
						if (result > tmp) {
							result = tmp;
						}
					}
					break;

				case 2:
					for (int a = 0; a < k; a++) {
						for (int b = 0; b < k; b++) {
							if (a == b) {
								continue;
							}
							long tmp = 0;
							tmp += dist[mid[a]];
							tmp += dij[a][mid[b]];
							tmp += dij[b][n - 1];
							if (result > tmp) {
								result = tmp;
							}
						}
					}
					break;
				case 3:
					for (int a = 0; a < k; a++) {
						for (int b = 0; b < k; b++) {
							if (a == b) {
								continue;
							}
							for (int c = 0; c < k; c++) {
								if (c == a || c == b) {
									continue;
								}
								long tmp = 0;
								tmp += dist[mid[a]];
								tmp += dij[a][mid[b]];
								tmp += dij[b][mid[c]];
								tmp += dij[c][n - 1];
								if (result > tmp) {
									result = tmp;
								}
							}
						}
					}
					break;

				case 4:
					for (int a = 0; a < k; a++) {
						for (int b = 0; b < k; b++) {
							if (a == b) {
								continue;
							}
							for (int c = 0; c < k; c++) {
								if (c == a || c == b) {
									continue;
								}
								for (int d = 0; d < k; d++) {
									if (d == a || d == b || d == c) {
										continue;
									}
									long tmp = 0;
									tmp += dist[mid[a]];
									tmp += dij[a][mid[b]];
									tmp += dij[b][mid[c]];
									tmp += dij[c][mid[d]];
									tmp += dij[d][n - 1];
									if (result > tmp) {
										result = tmp;
									}
								}
							}
						}
					}
					break;

				case 5:
					for (int a = 0; a < k; a++) {
						for (int b = 0; b < k; b++) {
							if (a == b) {
								continue;
							}
							for (int c = 0; c < k; c++) {
								if (c == a || c == b) {
									continue;
								}
								for (int d = 0; d < k; d++) {
									if (d == a || d == b || d == c) {
										continue;
									}
									for (int e = 0; e < k; e++) {
										if (e == a || e == b || e == c || e == d) {
											continue;
										}
										long tmp = 0;
										tmp += dist[mid[a]];
										tmp += dij[a][mid[b]];
										tmp += dij[b][mid[c]];
										tmp += dij[c][mid[d]];
										tmp += dij[d][mid[e]];
										tmp += dij[e][n - 1];
										if (result > tmp) {
											result = tmp;
										}
									}
								}
							}
						}
					}
					break;
				}

				out.println(result);
			}
		}

		public long[] dijkstra(int start, long[] dist, ArrayList<Node>[] citys, int n) {
			dist[start] = 0;
			for (int i = 0; i < citys[start].size(); i++) {
				int v = citys[start].get(i).to;
				int w = citys[start].get(i).w;
				if (dist[v] > w) {
					dist[v] = w;
				}
			}
			boolean[] visited = new boolean[n];
			for (int i = 0; i < visited.length; i++) {
				visited[i] = false;
			}
			visited[start] = true;
			for (int i = 0; i < n - 1; i++) {
				long minV = Long.MAX_VALUE;
				int mini = Integer.MAX_VALUE;
				for (int j = 0; j < n; j++) {
					if (!visited[j] && dist[j] < minV) {
						minV = dist[j];
						mini = j;
					}
				}
				visited[mini] = true;
				for (int j = 0; j < citys[mini].size(); j++) {
					int v = citys[mini].get(j).to;
					int w = citys[mini].get(j).w;
					if (!visited[v] && dist[v] > dist[mini] + w) {
						dist[v] = dist[mini] + w;
					}
				}
			}
			return dist;
		}
	}

	static class Node {
		int to;
		int w;

		public Node(int to, int w) {
			this.to = to;
			this.w = w;
		}
	}

	static class Reader {
		final private int BUFFER_SIZE = 1 << 16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer, bytesRead;

		public Reader() {
			din = new DataInputStream(System.in);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public Reader(String file_name) throws IOException {
			din = new DataInputStream(new FileInputStream(file_name));
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public String readLine() throws IOException {
			byte[] buf = new byte[64]; // line length
			int cnt = 0, c;
			while ((c = read()) != -1) {
				if (c == '\n')
					break;
				buf[cnt++] = (byte) c;
			}
			return new String(buf, 0, cnt);
		}

		public int nextInt() throws IOException {
			int ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (neg)
				return -ret;
			return ret;
		}

		public long nextLong() throws IOException {
			long ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');
			if (neg)
				return -ret;
			return ret;
		}

		public double nextDouble() throws IOException {
			double ret = 0, div = 1;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();

			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (c == '.') {
				while ((c = read()) >= '0' && c <= '9') {
					ret += (c - '0') / (div *= 10);
				}
			}

			if (neg)
				return -ret;
			return ret;
		}

		private void fillBuffer() throws IOException {
			bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			if (bytesRead == -1)
				buffer[0] = -1;
		}

		private byte read() throws IOException {
			if (bufferPointer == bytesRead)
				fillBuffer();
			return buffer[bufferPointer++];
		}

		public void close() throws IOException {
			if (din == null)
				return;
			din.close();
		}
	}
}
