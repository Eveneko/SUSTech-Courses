package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

public class B_kruskal {
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
				int[] parent = new int[n];
				for (int i = 0; i < parent.length; i++) {
					parent[i] = -1;
				}
				edge[] edges = new edge[m];

				for (int i = 0; i < m; i++) {
					edges[i] = new edge(0, 0, 0);
					edges[i].u = in.nextInt() - 1;
					edges[i].v = in.nextInt() - 1;
					edges[i].w = in.nextInt();
				}
				sortedge(edges);
				kruskal(edges, parent, n, m);
			}
		}

		public void kruskal(edge[] edges, int[] parent, int n, int m) {
			int sumweight = 0;
			int num = 0;// 已选用的边的数目
			int u, v;
			for (int i = 0; i < m; i++) {
				u = edges[i].u;
				v = edges[i].v;
				if (Find(u, parent) != Find(v, parent)) {
					sumweight += edges[i].w;
					num++;
					Union(u, v, parent);
				}
				if (num >= n - 1) {
					break;
				}
			}
			System.out.println(sumweight);
		}

		public int Find(int x, int[] parent) {
			int s = 0;
			for (s = x; parent[s] >= 0; s = parent[s]) {
			}
			while (s != x) {
				int tmp = parent[x];
				parent[x] = s;
				x = tmp;
			}
			return s;
		}

		public void Union(int R1, int R2, int[] parent) {
			int r1 = Find(R1, parent); // r1 为 R1 的根结点，r2 为 R2 的根结点
			int r2 = Find(R2, parent);
			int tmp = parent[r1] + parent[r2];// 如果 R2 所在树结点个数 > R1 所在树结点个数(注意 parent[r1]是负数)
			if (parent[r1] > parent[r2]) {
				parent[r1] = r2;
				parent[r2] = tmp;
			} else {
				parent[r2] = r1;
				parent[r1] = tmp;
			}
		}

		public void sortedge(edge[] edges) {
			int m = edges.length;
			Arrays.sort(edges, new Comparator<edge>() {
				public int compare(edge a, edge b) {
					return a.w - b.w;
				}
			});
		}
	}

	static class edge {
		int u, v, w;

		public edge(int u, int v, int w) {
			this.u = u;
			this.v = v;
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
