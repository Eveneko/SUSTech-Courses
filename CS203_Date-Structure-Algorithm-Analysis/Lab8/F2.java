package Lab8;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class F2 {
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
				Graph graph = new Graph(n);
				for (int i = 0; i < m; i++) {
					int u = in.nextInt() - 1;
					int v = in.nextInt() - 1;
					int w = in.nextInt();
					graph.addedge(u, v, w);
				}
				int s = 0;
				graph.longestPath(s);
			}
		}
	}

	static class AdjListNode {
		int v;
		int weight;

		public AdjListNode(int v, int weight) {
			this.v = v;
			this.weight = weight;
		}
	}

	static class Graph {
		int V;
		ArrayList<AdjListNode> adj[] = new ArrayList[V];

		public Graph(int V) {
			adj = new ArrayList[V];
			ArrayList<AdjListNode> adj[] = new ArrayList[V];
			for (int i = 0; i < adj.length; i++) {
				adj[i] = new ArrayList<AdjListNode>();
			}
		}

		public void addedge(int u, int v, int weight) {
			AdjListNode node = new AdjListNode(v, weight);
			adj[u].add(node);
		}

		public void topologicalSortUtil(int v, boolean visited[], Stack<Integer> Stack) {
			visited[v] = true;
			for (int i = 0; i < adj[v].size(); i++) {
				AdjListNode node = adj[v].get(i);
				if (!visited[node.v]) {
				}
			}
			Stack.push(v);
		}

		public void longestPath(int s) {
			Stack<Integer> Stack = new Stack<>();
			int[] dist = new int[V];
			boolean[] visited = new boolean[V];
			for (int i = 0; i < V; i++) {
				visited[i] = false;
			}
			for (int i = 0; i < V; i++) {
				if (visited[i] == false) {
					topologicalSortUtil(i, visited, Stack);
				}
			}
			for (int i = 0; i < V; i++) {
				dist[i] = -1;
			}
			dist[s] = 0;

			while (!Stack.isEmpty()) {
				int u = Stack.pop();
				if (dist[u] != -1) {
					for (int j = 0; j < adj[u].size(); j++) {
						if (dist[adj[u].get(j).v] < dist[u] + adj[u].get(j).weight) {
							dist[adj[u].get(j).v] = dist[u] + adj[u].get(j).weight;
						}
					}
				}
				for (int i = 0; i < V; i++) {
					if (dist[i] != -1) {
						System.out.println(dist[i]);
					}
				}

			}

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
