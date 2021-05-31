package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class H {
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
				int n = in.nextInt() + 2;
				int[][] xyzr = new int[n][4];
				for (int i = 0; i < n; i++) {
					xyzr[i][0] = in.nextInt();
					xyzr[i][1] = in.nextInt();
					xyzr[i][2] = in.nextInt();
					if (i >= n - 2) {
						xyzr[i][3] = 0;
					} else {
						xyzr[i][3] = in.nextInt();
					}
				}

				ArrayList<Node> holes[] = new ArrayList[n];
				for (int i = 0; i < n; i++) {
					holes[i] = new ArrayList<>();
					for (int j = 0; j < n; j++) {
						if (i == j) {
							continue;
						}
						Node pNode = new Node(j,
								Math.sqrt((xyzr[i][0] - xyzr[j][0]) * (xyzr[i][0] - xyzr[j][0])
										+ (xyzr[i][1] - xyzr[j][1]) * (xyzr[i][1] - xyzr[j][1])
										+ (xyzr[i][2] - xyzr[j][2]) * (xyzr[i][2] - xyzr[j][2])) - xyzr[i][3]
										- xyzr[j][3]);
						if (pNode.w < 0) {
							pNode.w = 0;
						}
						holes[i].add(pNode);
					}
				}
				double[] dist = new double[n];
				for (int i = 0; i < dist.length; i++) {
					dist[i] = Double.MAX_VALUE;
				}
				dist = dijkstra(n - 2, dist, holes, n);
				long result = 0;
				result = Math.round(100 * dist[n - 1]);
				out.println(result);
			}
		}

		public double[] dijkstra(int start, double[] dist, ArrayList<Node>[] holes, int n) {
			dist[start] = 0;
			for (int i = 0; i < holes[start].size(); i++) {
				int v = holes[start].get(i).to;
				double w = holes[start].get(i).w;
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
				double minV = Double.MAX_VALUE;
				int mini = Integer.MAX_VALUE;
				for (int j = 0; j < n; j++) {
					if (!visited[j] && dist[j] < minV) {
						minV = dist[j];
						mini = j;
					}
				}
				visited[mini] = true;
				for (int j = 0; j < holes[mini].size(); j++) {
					int v = holes[mini].get(j).to;
					double w = holes[mini].get(j).w;
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
		double w;

		public Node(int to, double w) {
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
