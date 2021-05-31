package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class I {
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
				// 记录坐标
				int[][] nodes = new int[n][2];
				for (int i = 0; i < nodes.length; i++) {
					nodes[i][0] = in.nextInt();
					nodes[i][1] = in.nextInt();
				}
				// 构造图
				ArrayList<Node> nodeslist[] = new ArrayList[n + m];
				for (int i = 0; i < n; i++) {
					nodeslist[i] = new ArrayList<>();
					for (int j = 0; j < n; j++) {
						if (i == j) {
							continue;
						}
						Node newnode = new Node(j, (nodes[i][0] - nodes[j][0]) * (nodes[i][0] - nodes[j][0])
								+ (nodes[i][1] - nodes[j][1]) * (nodes[i][1] - nodes[j][1]));
						nodeslist[i].add(newnode);
					}
				}

				int[][] sets = new int[m][];
				int[] setscast = new int[m];
				boolean[] inset = new boolean[n];
				// 每个点最短距离
				int[] dist = new int[n + m];
				int[][] alldist = new int[n][n];
				for (int i = 0; i < n + m; i++) {
					for (int j = 0; j < n + m; j++) {
						dist[j] = Integer.MAX_VALUE;
					}
					dijkstra(i, dist, nodeslist, n + m);
					for (int j = 0; j < n + m; j++) {
						alldist[i][j] = dist[j];
					}
				}
				// 构造集合，创建超级起点
				for (int i = 0; i < m; i++) {
					nodeslist[n + i] = new ArrayList<>();
					int[] set = new int[in.nextInt()];
					setscast[i] = in.nextInt();
					for (int j = 0; j < set.length; j++) {
						set[j] = in.nextInt() - 1;
						Node supernode = new Node(set[j], 0);
						nodeslist[n + i].add(supernode);
						Node vNode = new Node(n + i, 0);
						nodeslist[set[j]].add(vNode);
					}
					sets[i] = set;
					int dis = 0;
					for (int j = 0; j < set.length; j++) {
						dis += alldist[set[i]][set[j]];
					}
					if (setscast[i] > dis) {
						setscast[i] = dis;
					}
				}
				// 更新最短距离
				for (int i = 0; i < n + m; i++) {
					for (int j = 0; j < n + m; j++) {
						dist[j] = Integer.MAX_VALUE;
					}
					dijkstra(i, dist, nodeslist, n + m);
					for (int j = 0; j < n + m; j++) {
						alldist[i][j] = dist[j];
					}
				}
				// 累加距离
				int result = Integer.MAX_VALUE;
				for (int i = 0; i < Math.pow(2, m); i++) {
					int tmp = 0;
					for (int s = 0; s < inset.length; s++) {
						inset[s] = false;
					}
					String bin = Integer.toBinaryString(i);
					int len = bin.length();
					for (int j = 0; j < m - len; j++) {
						bin = "0" + bin;
					}
					for (int j = 0; j < m; j++) {
						if (bin.charAt(j) == '0') {
							continue;
						}
						for (int k = 0; k < sets[j].length; k++) {
							inset[sets[j][k]] = true;
						}
						tmp += setscast[j];
					}
					int l = 0;
					for (l = 0; l < n; l++) {
						if (!inset[l]) {
							break;
						}
					}
					for (int j = 0; j < n; j++) {
						if (inset[j]) {
							continue;
						}
						tmp += alldist[l][j];
					}

					if (tmp < result) {
						result = tmp;
					}
				}
				out.println(result);
			}
		}

		public int[] dijkstra(int start, int[] dist, ArrayList<Node>[] nodes, int n) {
			dist[start] = 0;
			for (int i = 0; i < nodes[start].size(); i++) {
				int v = nodes[start].get(i).to;
				int w = nodes[start].get(i).w;
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
				int minV = Integer.MAX_VALUE;
				int mini = Integer.MAX_VALUE;
				for (int j = 0; j < n; j++) {
					if (!visited[j] && dist[j] < minV) {
						minV = dist[j];
						mini = j;
					}
				}
				visited[mini] = true;
				for (int j = 0; j < nodes[mini].size(); j++) {
					int v = nodes[mini].get(j).to;
					int w = nodes[mini].get(j).w;
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
