package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class F_dijkstra {
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

				int Bob = in.nextInt() - 1;
				long[] dist_bob = new long[n];
				for (int i = 0; i < dist_bob.length; i++) {
					dist_bob[i] = Long.MAX_VALUE;
				}
				dist_bob = dijkstra(Bob, dist_bob, citys, n, m);

				int Alice = in.nextInt() - 1;
				long[] dist_alice = new long[n];
				for (int i = 0; i < dist_alice.length; i++) {
					dist_alice[i] = Long.MAX_VALUE;
				}
				dist_alice = dijkstra(Alice, dist_alice, citys, n, m);

				long result = Long.MAX_VALUE;
				for (int i = 0; i < n; i++) {
					out.println("Bob[" + i + "]:" + dist_bob[i] + "    Alice[" + i + "]:" + dist_alice[i]);
					long tmp = dist_alice[i] >= dist_bob[i] ? dist_alice[i] : dist_bob[i];
					if (result > tmp) {
						result = tmp;
					}
				}
				out.println(result);
			}
		}

		public long[] dijkstra(int start, long[] dist, ArrayList<Node>[] citys, int n, int m) {
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
